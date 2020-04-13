package com.play.ucenter.service;


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
    int giftPay(Long userId, BigDecimal totalPrice);
}
