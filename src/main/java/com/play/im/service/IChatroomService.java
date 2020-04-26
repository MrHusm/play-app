package com.play.im.service;

import com.play.base.exception.ServiceException;
import com.play.base.service.IBaseService;
import com.play.base.utils.PageFinder;
import com.play.base.utils.Query;
import com.play.im.model.Chatroom;
import com.play.im.view.ChatroomVO;
import com.play.im.view.RoomMicVO;
import com.play.ucenter.view.UserMicVO;
import com.play.ucenter.view.UserVO;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

public interface IChatroomService extends IBaseService<Chatroom,Long> {

    /**
     * 根据id获取聊天室信息
     * @param roomId
     * @return
     */
    ChatroomVO getByRoomId(Integer roomId);

    /**
     * 创建个人聊天室
     * @param userId
     * @param name 聊天室名称
     * @param tagType 聊天室标签类型 1：女神 2：男神 3：电台
     * @return
     */
    Integer createChatroom(Long userId, String name, Integer tagType) throws ServiceException ;

    /**
     * 获取个人聊天室列表
     * @param userId
     * @return
     */
    List<ChatroomVO> getMineList(Long userId);

    /**
     * 获取聊天室信息 麦位信息 排麦信息等
     *
     * @param userId
     * @param roomId
     * @param pwd    聊天室密码
     */
    Map<String, Object> getRoomInfo(Long userId, Integer roomId, Integer pwd) throws ServiceException;

    /**
     * 加入聊天室
     *
     * @param roomId
     * @param userId
     */
    void joinRoom(Integer roomId, Long userId);

    /**
     * 上麦
     *
     * @param userId    操作人id
     * @param micUserId 上麦人id
     * @param roomId
     * @param position  麦的位置
     * @throws ServiceException
     */
    void upMic(Long userId, Long micUserId, Integer roomId, Integer position) throws ServiceException;

    /**
     * 下麦
     *
     * @param micUserId
     * @param roomId
     */
    void downMic(Long micUserId, Integer roomId);

    /**
     * 取消排麦
     *
     * @param micUserId
     * @param roomId
     */
    void cancelUpMic(Long micUserId, Integer roomId);

    /**
     * 获取聊天室排麦列表
     *
     * @param roomId
     * @return
     */
    List<UserMicVO> roomMicQueue(Integer roomId);


    /**
     * 聊天室用户列表
     *
     * @param userId 查看人id
     * @param roomId
     */
    PageFinder<UserVO> getChatroomUsersByPage(Long userId, Integer roomId, Query query);

    /**
     * 用户禁言
     *
     * @param uid
     * @param userId
     * @param roomId
     * @param time
     */
    void addNospeak(Long uid, Long userId, Integer roomId, Integer time) throws ServiceException;

    /**
     * 解除用户禁言
     *
     * @param uid
     * @param userId
     * @param roomId
     */
    void removeNospeak(Long uid, Long userId, Integer roomId) throws ServiceException;

    /**
     * 禁言用户列表
     *
     * @param userId 查看人id
     * @param roomId
     * @return
     */
    List<UserVO> nospeakList(Long userId, Integer roomId);

    /**
     * 用户加入黑名单 踢出房间
     *
     * @param uid
     * @param userId
     * @param roomId
     */
    void addBlack(Long uid, Long userId, Integer roomId) throws ServiceException;

    /**
     * 用户移除黑名单
     *
     * @param uid
     * @param userId
     * @param roomId
     */
    void removeBlack(Long uid, Long userId, Integer roomId);

    /**
     * 房间用户黑名单列表
     *
     * @param userId
     * @param roomId
     * @return
     */
    List<UserVO> blackList(Long userId, Integer roomId);

    /**
     * 用户离开房间
     *
     * @param roomId
     * @param userId
     */
    void leave(Integer roomId, Long userId);

    /**
     * 聊天室关闭
     * @param userId
     * @param roomId
     */
    void close(Long userId, Integer roomId) throws ServiceException;

    /**
     * 聊天室开启
     * @param userId
     * @param roomId
     */
    void open(Long userId, Integer roomId) throws ServiceException;

    /**
     * 修改聊天室信息
     * @param chatroom
     */
    void updateChatroom(Long userId, ChatroomVO chatroom) throws ServiceException;

    /**
     * 开始麦位倒计时
     * @param userId
     * @param roomId
     * @param position
     * @param num
     */
    void startTimer(Long userId, Integer roomId, Integer position, Integer num) throws ServiceException;

    /**
     * 停止麦位倒计时
     * @param userId
     * @param roomId
     * @param position
     */
    void stopTimer(Long userId, Integer roomId, Integer position) throws ServiceException;

    /**
     * 开启禁麦
     *
     * @param userId
     * @param roomId
     * @param position
     */
    void openMic(Long userId, Integer roomId, Integer position) throws ServiceException;

    /**
     * 关闭禁麦
     *
     * @param userId
     * @param roomId
     * @param position
     */
    void closeMic(Long userId, Integer roomId, Integer position) throws ServiceException;

    /**
     * 聊天室加锁
     * @param userId
     * @param roomId
     * @param pwd
     */
    void lock(Long userId, Integer roomId, Integer pwd) throws ServiceException;

    /**
     * 聊天室解锁
     * @param userId
     * @param roomId
     */
    void unlock(Long userId, Integer roomId) throws ServiceException;

    /**
     * 增加麦位心动值
     * @param roomId
     * @param positions
     * @param heartValue
     */
    void addMicHeart(Integer roomId, List<Integer> positions, Integer heartValue);

    /**
     * 增加聊天室贡献和魅力值
     * @param roomId
     * @param userId
     * @param targetUserIds
     * @param worth
     */
    void addUserRoomRank(Integer roomId, Long userId, List<Long> targetUserIds, Integer worth);


    /**
     * 获取聊天室麦位信息
     *
     * @param roomId
     * @return
     */
    Future<List<RoomMicVO>> getRoomMicInfo(Integer roomId);

}
