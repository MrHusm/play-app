package com.play.ucenter.dao.impl;

import com.play.base.dao.impl.BaseDaoImpl;
import com.play.ucenter.dao.IUserAccountDao;
import com.play.ucenter.model.UserAccount;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hushengmeng on 2017/7/4.
 */
@Repository(value="userAccountDao")
public class UserAccountDaoImpl extends BaseDaoImpl<UserAccount> implements IUserAccountDao {
    @Override
    public int giftPay(Long userId, BigDecimal amount) {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("userId", userId);
        param.put("amount", amount);
        return this.update("UserAccountMapper.giftPay", param);
    }
}
