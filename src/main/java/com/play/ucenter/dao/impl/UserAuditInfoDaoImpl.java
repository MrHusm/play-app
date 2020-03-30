package com.play.ucenter.dao.impl;

import com.play.base.dao.impl.BaseDaoImpl;
import com.play.ucenter.dao.IUserAuditInfoDao;
import com.play.ucenter.model.UserAuditInfo;
import org.springframework.stereotype.Repository;

/**
 * Created by hushengmeng on 2017/7/4.
 */
@Repository(value = "userAuditInfoDao")
public class UserAuditInfoDaoImpl extends BaseDaoImpl<UserAuditInfo> implements IUserAuditInfoDao {
}
