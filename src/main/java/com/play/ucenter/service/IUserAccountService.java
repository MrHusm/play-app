package com.play.ucenter.service;


import com.play.base.exception.ServiceException;
import com.play.base.service.IBaseService;
import com.play.ucenter.model.UserAccount;

import java.math.BigDecimal;

/**
 * Created by hushengmeng on 2017/7/4.
 */
public interface IUserAccountService extends IBaseService<UserAccount,Long> {

    /**
     * 根据用户id 获取账户信息
     * @param userId
     * @return
     */
    UserAccount getByUserId(Long userId);

    /**
     * 支付礼物
     *
     * @param userId
     * @param totalPrice
     */
    int silverPay(Long userId, BigDecimal totalPrice);

    /**
     * 收到礼物
     *
     * @param userId
     * @param amount
     * @return
     */
    int addGold(Long userId, BigDecimal amount);

    /**
     * 转账
     *
     * @param userId
     * @param targetUserId
     * @param amount
     */
    void transferAccount(Long userId, Long targetUserId, BigDecimal amount) throws ServiceException;

    /**
     * 用户兑换
     *
     * @param userId
     * @param amount
     */
    void exchange(Long userId, BigDecimal amount) throws ServiceException;

    /**
     * 管理员充值
     *
     * @param userId
     * @param amount
     */
    void adminRecharge(Long userId, Long targetUserId, BigDecimal amount) throws ServiceException;
}
