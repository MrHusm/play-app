package com.play.ucenter.service;


import com.play.base.exception.ServiceException;
import com.play.base.service.IBaseService;
import com.play.ucenter.model.User;

/**
 * Created by hushengmeng on 2017/7/4.
 */
public interface IUserService extends IBaseService<User,Long> {

    /**
     * 用户退出系统
     *
     * @param token
     */
    void logout(String token);

    /**
     * 发送验证码
     *
     * @param mobile
     */
    void sendMobileCode(String mobile) throws ServiceException;
}
