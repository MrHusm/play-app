package com.play.ucenter.service;


import com.play.base.exception.ServiceException;
import com.play.base.service.IBaseService;
import com.play.ucenter.model.User;
import com.play.ucenter.model.UserAccount;
import com.play.ucenter.view.UserView;

import java.util.List;
import java.util.Map;

/**
 * Created by hushengmeng on 2017/7/4.
 */
public interface IUserAccountService extends IBaseService<UserAccount,Long> {

    /**
     * 根据用户id 获取账户信息
     * @param userId
     * @return
     */
    UserAccount getByUserId(Long userId);
}
