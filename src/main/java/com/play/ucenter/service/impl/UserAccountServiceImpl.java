package com.play.ucenter.service.impl;

import com.play.base.dao.IBaseDao;
import com.play.base.exception.ServiceException;
import com.play.base.service.impl.BaseServiceImpl;
import com.play.base.utils.ResultCustomMessage;
import com.play.ucenter.dao.IUserAccountDao;
import com.play.ucenter.model.TradeRecord;
import com.play.ucenter.model.UserAccount;
import com.play.ucenter.service.ITradeRecordService;
import com.play.ucenter.service.IUserAccountService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

/**
 * Created by hushengmeng on 2020/3/30.
 */
@Service(value="userAccountService")
public class UserAccountServiceImpl extends BaseServiceImpl<UserAccount, Long> implements IUserAccountService {

    @Resource
    private IUserAccountDao userAccountDao;
    @Resource
    private ITradeRecordService tradeRecordService;

    @Override
    public IBaseDao<UserAccount> getBaseDao() {
        return userAccountDao;
    }

    @Override
    public UserAccount getByUserId(Long userId) {
        return this.findUniqueByParams("userId",userId);
    }

    @Override
    public int silverPay(Long userId, BigDecimal amount) {
        return userAccountDao.silverPay(userId, amount);
    }

    @Override
    public int addGold(Long userId, BigDecimal amount) {
        return userAccountDao.addGold(userId, amount);
    }

    @Transactional
    @Override
    public void transferAccount(Long userId, Long targetUserId, BigDecimal amount) throws ServiceException {
        UserAccount userAccount = this.getByUserId(userId);
        if (userAccount.getSilverAmount().compareTo(amount) < 0) {
            throw new ServiceException(ResultCustomMessage.F1012);
        }
        UserAccount targetUserAccount = this.getByUserId(targetUserId);
        if (targetUserAccount == null) {
            throw new ServiceException(ResultCustomMessage.F1016);
        }
        int updateStatus = silverPay(userId, amount);
        if (updateStatus != 1) {
            //扣除失败
            throw new ServiceException(ResultCustomMessage.F1012);
        }
        String orderNo = UUID.randomUUID().toString().replaceAll("-", "");
        Date now = new Date();
        //保存账户交易记录
        TradeRecord tradeRecord = new TradeRecord();
        tradeRecord.setOrderNo(orderNo);
        tradeRecord.setType(-2);
        tradeRecord.setAmount(amount);
        tradeRecord.setAccountType(2);
        tradeRecord.setTradeTime(now);
        tradeRecord.setUserId(userId);
        tradeRecord.setCreator(userId);
        tradeRecord.setCreateDate(now);
        tradeRecordService.save(tradeRecord);


        silverPay(targetUserId, amount.negate());
        //保存账户交易记录
        TradeRecord targetTradeRecord = new TradeRecord();
        targetTradeRecord.setOrderNo(orderNo);
        targetTradeRecord.setType(5);
        targetTradeRecord.setAmount(amount);
        targetTradeRecord.setAccountType(2);
        targetTradeRecord.setTradeTime(now);
        targetTradeRecord.setUserId(targetUserId);
        targetTradeRecord.setCreator(targetUserId);
        targetTradeRecord.setCreateDate(now);
        tradeRecordService.save(targetTradeRecord);
    }

    @Transactional
    @Override
    public void exchange(Long userId, BigDecimal amount) throws ServiceException {
        UserAccount userAccount = this.getByUserId(userId);
        if (userAccount.getSilverAmount().compareTo(amount) < 0) {
            throw new ServiceException(ResultCustomMessage.F1012);
        }

        int updateStatus = userAccountDao.exchange(userId, amount);
        if (updateStatus != 1) {
            //扣除失败
            throw new ServiceException(ResultCustomMessage.F1012);
        }
        String orderNo = UUID.randomUUID().toString().replaceAll("-", "");
        Date now = new Date();
        //保存账户交易记录
        TradeRecord tradeRecord = new TradeRecord();
        tradeRecord.setOrderNo(orderNo);
        tradeRecord.setType(7);
        tradeRecord.setAmount(amount);
        tradeRecord.setAccountType(1);
        tradeRecord.setTradeTime(now);
        tradeRecord.setUserId(userId);
        tradeRecord.setCreator(userId);
        tradeRecord.setCreateDate(now);
        tradeRecordService.save(tradeRecord);
    }

    @Transactional
    @Override
    public void adminRecharge(Long userId, Long targetUserId, BigDecimal amount) throws ServiceException {
        if (userId != 88888888L) {
            throw new ServiceException(ResultCustomMessage.F1017);
        }
        String orderNo = UUID.randomUUID().toString().replaceAll("-", "");
        silverPay(targetUserId, amount.negate());
        //保存账户交易记录
        Date now = new Date();
        TradeRecord targetTradeRecord = new TradeRecord();
        targetTradeRecord.setOrderNo(orderNo);
        targetTradeRecord.setType(4);
        targetTradeRecord.setAmount(amount);
        targetTradeRecord.setAccountType(2);
        targetTradeRecord.setTradeTime(now);
        targetTradeRecord.setUserId(targetUserId);
        targetTradeRecord.setCreator(targetUserId);
        targetTradeRecord.setCreateDate(now);
        tradeRecordService.save(targetTradeRecord);
    }
}
