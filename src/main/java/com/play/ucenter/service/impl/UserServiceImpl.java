package com.play.ucenter.service.impl;

import com.play.base.dao.IBaseDao;
import com.play.base.service.impl.BaseServiceImpl;
import com.play.ucenter.dao.IUserDao;
import com.play.ucenter.model.User;
import com.play.ucenter.service.IUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by hushengmeng on 2017/7/4.
 */
@Service(value="userCmsService")
public class UserServiceImpl extends BaseServiceImpl<User, Long> implements IUserService {

    @Resource
    private IUserDao userCmsDao;

    @Override
    public IBaseDao<User> getBaseDao() {
        return userCmsDao;
    }

}
