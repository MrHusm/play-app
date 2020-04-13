package com.play.ucenter.service.impl;

import com.play.base.dao.IBaseDao;
import com.play.base.service.impl.BaseServiceImpl;
import com.play.ucenter.dao.IUserAccountDao;
import com.play.ucenter.model.UserAccount;
import com.play.ucenter.service.IUserAccountService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * Created by hushengmeng on 2020/3/30.
 */
@Service(value="userAccountService")
public class UserAccountServiceImpl extends BaseServiceImpl<UserAccount, Long> implements IUserAccountService {

    @Resource
    private IUserAccountDao userAccountDao;

    @Override
    public IBaseDao<UserAccount> getBaseDao() {
        return userAccountDao;
    }

    @Override
    public UserAccount getByUserId(Long userId) {
        return this.findUniqueByParams("userId",userId);
    }

    @Override
    public int giftPay(Long userId, BigDecimal amount) {
        return userAccountDao.giftPay(userId, amount);
    }
}
