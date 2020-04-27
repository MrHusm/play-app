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
    int silverPay(Long userId, BigDecimal amount);

    /**
     * 收到礼物
     * @param userId
     * @param amount
     * @return
     */
    int addGold(Long userId, BigDecimal amount);

    /**
     * 兑换
     *
     * @param userId
     * @param amount
     * @return
     */
    int exchange(Long userId, BigDecimal amount);
}
