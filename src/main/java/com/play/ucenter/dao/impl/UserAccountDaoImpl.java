package com.play.ucenter.dao.impl;

import com.play.base.dao.impl.BaseDaoImpl;
import com.play.ucenter.dao.IUserAccountDao;
import com.play.ucenter.dao.IUserDao;
import com.play.ucenter.model.User;
import com.play.ucenter.model.UserAccount;
import org.springframework.stereotype.Repository;

/**
 * Created by hushengmeng on 2017/7/4.
 */
@Repository(value="userAccountDao")
public class UserAccountDaoImpl extends BaseDaoImpl<UserAccount> implements IUserAccountDao {
}
