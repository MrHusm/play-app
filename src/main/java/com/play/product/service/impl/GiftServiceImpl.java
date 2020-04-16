package com.play.product.service.impl;

import com.play.base.contants.RedisKeyConstants;
import com.play.base.dao.IBaseDao;
import com.play.base.exception.ServiceException;
import com.play.base.service.impl.BaseServiceImpl;
import com.play.base.utils.BeanUtils;
import com.play.base.utils.ResultCustomMessage;
import com.play.im.service.IChatroomService;
import com.play.im.view.ChatroomVO;
import com.play.message.view.GiftMessageVO;
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
import org.springframework.amqp.core.AmqpTemplate;
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
    private IUserAccountService userAccountService;
    @Resource
    private ITradeRecordService tradeRecordService;
    @Resource
    private IChatroomService chatroomService;
    @Resource
    private AmqpTemplate amqpTemplate;
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
        Date sendDate = new Date();
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
            tradeRecord.setAccountType(2);
            tradeRecord.setTradeTime(sendDate);
            tradeRecord.setUserId(userId);
            tradeRecord.setCreator(userId);
            tradeRecord.setCreateDate(sendDate);
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
        //增加麦位收益
        this.chatroomService.addMicHeart(roomId,positions,gift.getHeartValue()* giftNum);
        //发送礼物打赏消息
        GiftMessageVO message = new GiftMessageVO();
        message.setRoomId(roomId);
        message.setGift(gift);
        message.setGiftNum(giftNum);
        message.setSenderId(userId);
        message.setTargetUserIds(targetUserIds);
        message.setOrderNo(orderNo);
        message.setPayType(payType);
        message.setSendDate(sendDate);
        amqpTemplate.convertAndSend("queueGiftKey",message);

        //发送礼物消息 TODO

        //发送全服广播 TODO

    }

}
