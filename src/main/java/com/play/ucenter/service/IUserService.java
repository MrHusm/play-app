package com.play.ucenter.service;


import com.play.base.exception.ServiceException;
import com.play.base.service.IBaseService;
import com.play.ucenter.model.User;
import com.play.ucenter.view.UserView;

import java.util.List;
import java.util.Map;

/**
 * Created by hushengmeng on 2017/7/4.
 */
public interface IUserService extends IBaseService<User,Long> {

    /**
     * 根据用户Id获取用户信息
     * @param userId
     * @return
     */
    User getByUserId(Long userId);

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

    /**
     * 手机号登录
     * @param mobile
     * @param code
     * @param loginUser
     */
    Map<String,Object> loginByMobile(String mobile, String code, User loginUser)throws ServiceException;

    /**
     * 修改用户信息
     * @param user
     */
    void updateUser(User user);

    /**
     * 根据关键词搜索用户
     * @param keyword
     * @return
     */
    List<UserView> search(String keyword);
}
