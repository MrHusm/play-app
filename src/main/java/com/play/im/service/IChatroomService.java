package com.play.im.service;

import com.play.base.exception.ServiceException;
import com.play.base.service.IBaseService;
import com.play.im.model.Chatroom;
import com.play.im.view.ChatroomVO;

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
}
