package com.play.ucenter.service.impl;

import com.play.base.dao.IBaseDao;
import com.play.base.service.impl.BaseServiceImpl;
import com.play.ucenter.dao.IUserAuditInfoDao;
import com.play.ucenter.model.UserAuditInfo;
import com.play.ucenter.service.IUserAuditInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by hushengmeng on 2020/3/30.
 */
@Service(value = "userAuditInfoService")
public class UserAuditInfoServiceImpl extends BaseServiceImpl<UserAuditInfo, Long> implements IUserAuditInfoService {

    @Resource
    private IUserAuditInfoDao userAuditInfoDao;

    @Override
    public IBaseDao<UserAuditInfo> getBaseDao() {
        return userAuditInfoDao;
    }
}
