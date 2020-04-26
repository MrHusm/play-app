package com.play.message.listener;

import com.alibaba.fastjson.JSON;
import com.play.im.service.IChatroomService;
import com.play.im.view.ChatroomVO;
import com.play.message.view.GiftMessageVO;
import com.play.product.model.RewardRecord;
import com.play.product.service.IRewardRecordService;
import com.play.product.view.GiftVO;
import com.play.ucenter.model.TradeRecord;
import com.play.ucenter.model.User;
import com.play.ucenter.service.ITradeRecordService;
import com.play.ucenter.service.IUserAccountService;
import com.play.ucenter.service.IUserService;
import com.play.ucenter.view.UserVO;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by lenovo on 2020/4/16.
 */
public class GiftListener implements ChannelAwareMessageListener {
    private static final Logger logger = LoggerFactory.getLogger(GiftListener.class);

    private static Long platformUserId = 88888888L;

    @Resource
    private IUserService userService;
    @Resource
    private IUserAccountService userAccountService;
    @Resource
    private ITradeRecordService tradeRecordService;
    @Resource
    private IChatroomService chatroomService;
    @Resource
    private IRewardRecordService rewardRecordService;

    @Override
    public void onMessage(Message message, Channel channel) {
        String json = new String(message.getBody());
        try {
            logger.info("收到礼物打赏消息：{}", json);
            GiftMessageVO giftMessage = JSON.parseObject(json, GiftMessageVO.class);
            Integer roomId = giftMessage.getRoomId();
            Long userId = giftMessage.getSenderId();
            List<Long> targetUserIds = giftMessage.getTargetUserIds();
            Integer giftNum = giftMessage.getGiftNum();
            GiftVO gift = giftMessage.getGift();
            Integer payType = giftMessage.getPayType();
            String orderNo = giftMessage.getOrderNo();
            Date sendDate = giftMessage.getSendDate();
            ChatroomVO chatroom = this.chatroomService.getByRoomId(roomId);
            //保存礼物打赏记录
            List<RewardRecord> rewardRecords = new ArrayList<RewardRecord>();
            for (Long targetUserId : targetUserIds) {
                RewardRecord rewardRecord = new RewardRecord();
                rewardRecord.setOrderNo(orderNo);
                rewardRecord.setGiftId(gift.getId());
                rewardRecord.setGiftName(gift.getName());
                rewardRecord.setGiftNum(giftNum);
                rewardRecord.setGiftPrice(gift.getPrice());
                rewardRecord.setGiftTotalPrice(gift.getPrice().multiply(BigDecimal.valueOf(giftNum)));
                rewardRecord.setPayType(payType);
                rewardRecord.setReceiverId(targetUserId);
                rewardRecord.setRoomOwnerId(chatroom.getOwnnerId());
                rewardRecord.setRoomId(roomId);
                rewardRecord.setSenderId(userId);
                rewardRecord.setReceiverIncome(gift.getPrice().multiply(BigDecimal.valueOf(giftNum)).multiply(BigDecimal.valueOf(0.7)).setScale(2, BigDecimal.ROUND_HALF_UP));
                rewardRecord.setRoomOwnerIncome(gift.getPrice().multiply(BigDecimal.valueOf(giftNum)).multiply(BigDecimal.valueOf(0.1)).setScale(2, BigDecimal.ROUND_HALF_UP));
                rewardRecord.setPlatformIncome(gift.getPrice().multiply(BigDecimal.valueOf(giftNum)).multiply(BigDecimal.valueOf(0.1)).setScale(2, BigDecimal.ROUND_HALF_UP));
                rewardRecord.setCreator(userId);
                rewardRecord.setCreateDate(new Date());
                rewardRecords.add(rewardRecord);
            }
            rewardRecordService.save(rewardRecords);

            //升级VIP
            UserVO userVO = this.userService.getByUserId(userId, userId);
            Integer vipExp = userVO.getVipExp() + gift.getPrice().multiply(BigDecimal.valueOf(giftNum)).multiply(BigDecimal.valueOf(targetUserIds.size())).intValue();
            Integer vipLevel = userVO.getVipLevel();
            Integer nexVipExp = userService.getNextVipExp(vipLevel);
            while (vipExp >= nexVipExp) {
                // 发送系统通知 TODO
//            String msgContent = "恭喜您升级到VIP" + nextLevelInfo.getVipLevel() + "，请前往【我的】-【会员中⼼】查看详情。";
//            rongyunService.systemNoticeMessageAsync("会员中心", msgContent, userId, "会员中心-VIP升级通知");

                vipLevel++;
                nexVipExp = userService.getNextVipExp(vipLevel + 1);
            }
            User user = new User();
            user.setUserId(userVO.getUserId());
            user.setVipExp(vipExp);
            user.setVipLevel(vipLevel);
            userService.updateUser(user);

            //增加用户礼物墙
            for (Long targetUserId : targetUserIds) {
                userService.addUserGiftWall(targetUserId, gift.getId(), giftNum);
            }
            //增加贡献和魅力排行榜
            this.chatroomService.addUserRoomRank(roomId, userId, targetUserIds, gift.getWorth().intValue() * giftNum);
            //增加收礼人、工会、平台收益和记录
            for (Long targetUserId : targetUserIds) {
                //接收人增加收益
                userAccountService.giftReceive(targetUserId, gift.getPrice().multiply(BigDecimal.valueOf(giftNum)).multiply(BigDecimal.valueOf(0.7)).setScale(2, BigDecimal.ROUND_HALF_UP));
                //保存账户交易记录
                TradeRecord receiveTradeRecord = new TradeRecord();
                receiveTradeRecord.setOrderNo(orderNo);
                receiveTradeRecord.setType(1);
                receiveTradeRecord.setAmount(gift.getPrice().multiply(BigDecimal.valueOf(giftNum)).multiply(BigDecimal.valueOf(0.7)).setScale(2, BigDecimal.ROUND_HALF_UP));
                receiveTradeRecord.setAccountType(1);
                receiveTradeRecord.setTradeTime(sendDate);
                receiveTradeRecord.setUserId(targetUserId);
                receiveTradeRecord.setCreator(targetUserId);
                receiveTradeRecord.setCreateDate(sendDate);
                tradeRecordService.save(receiveTradeRecord);

                //公会增加收益
                userAccountService.giftReceive(chatroom.getOwnnerId(), gift.getPrice().multiply(BigDecimal.valueOf(giftNum)).multiply(BigDecimal.valueOf(0.1)).setScale(2, BigDecimal.ROUND_HALF_UP));
                //保存账户交易记录
                TradeRecord ownnerTradeRecord = new TradeRecord();
                ownnerTradeRecord.setOrderNo(orderNo);
                ownnerTradeRecord.setType(6);
                ownnerTradeRecord.setAmount(gift.getPrice().multiply(BigDecimal.valueOf(giftNum)).multiply(BigDecimal.valueOf(0.1)).setScale(2, BigDecimal.ROUND_HALF_UP));
                ownnerTradeRecord.setAccountType(1);
                ownnerTradeRecord.setTradeTime(sendDate);
                ownnerTradeRecord.setUserId(chatroom.getOwnnerId());
                ownnerTradeRecord.setCreator(chatroom.getOwnnerId());
                ownnerTradeRecord.setCreateDate(sendDate);
                tradeRecordService.save(ownnerTradeRecord);

                //平台增加收益
                userAccountService.giftReceive(platformUserId, gift.getPrice().multiply(BigDecimal.valueOf(giftNum)).multiply(BigDecimal.valueOf(0.1)).setScale(2, BigDecimal.ROUND_HALF_UP));
                //保存账户交易记录
                TradeRecord platformTradeRecord = new TradeRecord();
                platformTradeRecord.setOrderNo(orderNo);
                platformTradeRecord.setType(6);
                platformTradeRecord.setAmount(gift.getPrice().multiply(BigDecimal.valueOf(giftNum)).multiply(BigDecimal.valueOf(0.1)).setScale(2, BigDecimal.ROUND_HALF_UP));
                platformTradeRecord.setAccountType(1);
                platformTradeRecord.setTradeTime(new Date());
                platformTradeRecord.setUserId(platformUserId);
                platformTradeRecord.setCreator(platformUserId);
                platformTradeRecord.setCreateDate(new Date());
                tradeRecordService.save(platformTradeRecord);
            }
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            logger.info("处理礼物打赏消息异常：{}", json);
            e.printStackTrace();
            try {
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
//                channel.basicRecover(true);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }
}
