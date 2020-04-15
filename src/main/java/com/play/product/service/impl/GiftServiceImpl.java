package com.play.product.service.impl;

import com.play.base.contants.RedisKeyConstants;
import com.play.base.dao.IBaseDao;
import com.play.base.exception.ServiceException;
import com.play.base.service.impl.BaseServiceImpl;
import com.play.base.utils.BeanUtils;
import com.play.base.utils.ResultCustomMessage;
import com.play.im.service.IChatroomService;
import com.play.im.view.ChatroomVO;
import com.play.product.dao.IGiftDao;
import com.play.product.model.Gift;
import com.play.product.model.RewardRecord;
import com.play.product.service.IGiftService;
import com.play.product.service.IRewardRecordService;
import com.play.product.view.GiftVO;
import com.play.ucenter.model.TradeRecord;
import com.play.ucenter.model.User;
import com.play.ucenter.model.UserAccount;
import com.play.ucenter.service.ITradeRecordService;
import com.play.ucenter.service.IUserAccountService;
import com.play.ucenter.service.IUserService;
import com.play.ucenter.view.UserVO;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Created by hushengmeng on 2020/3/30.
 */
@Service(value = "giftService")
public class GiftServiceImpl extends BaseServiceImpl<Gift, Integer> implements IGiftService {
    @Resource
    private IGiftDao giftDao;
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
    @Resource(name = "redisTemplate")
    private RedisTemplate<String, Gift> giftRedisTemplate;
    @Resource(name = "redisTemplate")
    private RedisTemplate<String, Integer> intRedisTemplate;

    @Override
    public IBaseDao<Gift> getBaseDao() {
        return giftDao;
    }

    @Override
    public GiftVO getById(Integer id) {
        String key = String.format(RedisKeyConstants.CACHE_GIFT_ID_KEY, id);
        Gift gift = giftRedisTemplate.opsForValue().get(key);
        if (gift == null) {
            gift = get(id);
            if (gift != null) {
                giftRedisTemplate.opsForValue().set(key, gift, 1, TimeUnit.DAYS);
            }
        }
        GiftVO giftVO = BeanUtils.copyProperties(GiftVO.class, gift);
        return giftVO;
    }

    @Override
    public void sendGift(Long userId, Integer roomId, Integer giftId, Integer giftNum, List<Long> targetUserIds, List<Integer> positions, Integer payType) throws ServiceException {
        GiftVO gift = this.getById(giftId);
        if (gift.getStatus() == 0) {
            throw new ServiceException(ResultCustomMessage.F1011);
        }
        String orderNo = UUID.randomUUID().toString().replaceAll("-", "");
        BigDecimal amount = gift.getPrice().multiply(BigDecimal.valueOf(giftNum)).multiply(BigDecimal.valueOf(targetUserIds.size()));
        if (payType == 0) {
            //使用金币打赏
            UserAccount userAccount = this.userAccountService.getByUserId(userId);
            if (userAccount.getSilverAmount().compareTo(amount) < 0) {
                throw new ServiceException(ResultCustomMessage.F1012);
            }
            //扣除银币
            int updateStatus = userAccountService.giftPay(userId, amount);
            if (updateStatus != 1) {
                //扣除失败
                throw new ServiceException(ResultCustomMessage.F1012);
            }
            //保存账户交易记录
            TradeRecord tradeRecord = new TradeRecord();
            tradeRecord.setOrderNo(orderNo);
            tradeRecord.setType(-1);
            tradeRecord.setAmount(amount);
            tradeRecord.setAccountType(1);
            tradeRecord.setTradeTime(new Date());
            tradeRecord.setUserId(userId);
            tradeRecord.setCreator(userId);
            tradeRecord.setCreateDate(new Date());
            tradeRecordService.save(tradeRecord);
        } else {
            //使用背包礼物打赏
            String key = String.format(RedisKeyConstants.CACHE_USER_PACK_GIFT_KEY, userId);
            Object num = intRedisTemplate.opsForHash().get(key, giftId);
            if (num == null || Integer.parseInt(String.valueOf(num)) < giftNum * targetUserIds.size()) {
                throw new ServiceException(ResultCustomMessage.F1013);
            }
            //扣除背包礼物
            intRedisTemplate.opsForHash().increment(key, giftId, giftNum * targetUserIds.size() * -1);
            num = intRedisTemplate.opsForHash().get(key, giftId);
            if (num == null || Integer.parseInt(String.valueOf(num)) < 0) {
                //背包礼物不足
                intRedisTemplate.opsForHash().increment(key, giftId, giftNum * targetUserIds.size());
                throw new ServiceException(ResultCustomMessage.F1013);
            }
        }
        ChatroomVO chatroom = this.chatroomService.getByRoomId(roomId);
        //保存礼物打赏记录
        List<RewardRecord> rewardRecords = new ArrayList<RewardRecord>();
        for (Long targetUserId : targetUserIds) {
            RewardRecord rewardRecord = new RewardRecord();
            rewardRecord.setOrderNo(orderNo);
            rewardRecord.setGiftId(giftId);
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
            rewardRecords.add(rewardRecord);

        }
        rewardRecordService.save(rewardRecords);

        //升级VIP
        UserVO userVO = this.userService.getByUserId(userId,userId);
        Integer vipExp = userVO.getVipExp() + amount.intValue();
        Integer vipLevel = userVO.getVipLevel();
        Integer nexVipExp = userService.getNextVipExp(vipLevel + 1);
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
        for(Long targetUserId : targetUserIds){
            userService.addUserGiftWall(targetUserId,giftId,giftNum);
        }
        //增加麦位收益
        this.chatroomService.addMicHeart(roomId,positions,gift.getHeartValue()* giftNum);
        //增加贡献和魅力排行榜
        this.chatroomService.addUserRoomRank(roomId,userId,targetUserIds,gift.getWorth().intValue() * giftNum);

        //发送礼物消息 TODO

        //发送全服广播 TODO

    }


}
