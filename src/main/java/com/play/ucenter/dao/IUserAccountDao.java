package com.play.ucenter.dao;


import com.play.base.dao.IBaseDao;
import com.play.ucenter.model.UserAccount;

import java.math.BigDecimal;

/**
 * Created by hushengmeng on 2017/7/4.
 */
public interface IUserAccountDao extends IBaseDao<UserAccount> {
    /**
     * 礼物支付
     *
     * @param userId
     * @param amount
     */
    int giftPay(Long userId, BigDecimal amount);
}
