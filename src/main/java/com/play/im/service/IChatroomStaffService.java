package com.play.im.service;

import com.play.base.service.IBaseService;
import com.play.im.model.ChatroomStaff;

import java.util.List;

public interface IChatroomStaffService extends IBaseService<ChatroomStaff,Long> {
    /**
     * 获取用户在聊天室的角色
     *
     * @param roomId
     * @param userId
     * @return
     */
    List<Integer> getRoomUserRole(Integer roomId, Long userId);
}
