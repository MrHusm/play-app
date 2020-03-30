package com.play.ucenter.service;


import com.play.base.exception.ServiceException;
import com.play.base.service.IBaseService;
import com.play.base.utils.PageFinder;
import com.play.base.utils.Query;
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
     * @param userId 调用者用户id
     * @param targetUserId
     * @return
     */
    UserView getByUserId(Long userId, Long targetUserId);

    /**
     * 删除用户缓存
     *
     * @param userId
     */
    void deleteUserCache(Long userId);
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

    /**
     * 获取用户关系（0: 没关系 1:已关注 2:好友 9:自己）
     */
    Integer getRelType(Long userId, Long toUserId);

    /**
     * 获取关系链数量
     *
     * @param userId
     * @param type   1:关注 2：粉丝 3：好友 4：访客
     * @return
     */
    Integer getRelationNum(Integer type, Long userId);

    /**
     * 添加访客
     *
     * @param mId
     * @param userId
     */
    void addVisit(Long mId, Long userId);

    /**
     * 添加关注
     *
     * @param mId
     * @param userId
     */
    void addFollow(Long mId, Long userId);

    /**
     * 取消关注
     *
     * @param mId
     * @param userId
     */
    void unFollow(Long mId, Long userId);

    /**
     * 获取关注列表
     *
     * @param userId
     * @param query
     * @param type   1:关注 2：粉丝 3：好友 4：访客
     * @return
     */
    PageFinder getRelationListByPager(Integer type, Long userId, Query query);
}
