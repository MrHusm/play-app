package com.play.im.service;

import com.play.base.exception.ServiceException;
import com.play.base.service.IBaseService;
import com.play.im.model.Chatroom;
import com.play.im.view.ChatroomVO;
import com.play.ucenter.view.UserMicVO;

import java.util.List;

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
     * 加入聊天室
     *
     * @param userId
     * @param roomId
     * @param pwd    聊天室密码
     */
    void join(Long userId, Integer roomId, Integer pwd) throws ServiceException;

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


}
