package com.play.ucenter.service;


import com.play.base.exception.ServiceException;
import com.play.base.service.IBaseService;
import com.play.base.utils.PageFinder;
import com.play.base.utils.Query;
import com.play.product.view.GiftVO;
import com.play.ucenter.model.User;
import com.play.ucenter.view.UserVO;

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
    UserVO getByUserId(Long userId, Long targetUserId);

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
    void logout(String token, Long userId);

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
    List<UserVO> search(String keyword);

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

    /**
     * 获取用户在线状态
     *
     * @param userId
     */
    String getOnlineTime(Long userId);

    Long verifyToken(String token);

    /**
     * 聊天室添加收藏
     * @param userId
     * @param roomId
     */
    void addCollection(Long userId, Integer roomId);

    /**
     * 取消聊天室收藏
     * @param userId
     * @param roomId
     */
    void removeCollection(Long userId, Integer roomId);

    /**
     * 获取下一级VIP的经验值
     * @param vipLevel
     * @return
     */
    Integer getNextVipExp(Integer vipLevel);

    /**
     * 增加用户礼物墙
     * @param userId
     * @param giftId
     * @param num
     */
    void addUserGiftWall(Long userId,Integer giftId,Integer num);

    /**
     * 获取用户礼物墙信息
     *
     * @param userId
     * @return
     */
    List<GiftVO> getUserGiftWall(Long userId);

    /**
     * 生成userId
     */
    void userIdGenerate(Long startUserId) throws ServiceException;

    /**
     * 生成roomId
     * @param l
     * @throws ServiceException
     */
    void roomIdGenerate(Long startRoomId)throws ServiceException;
}
