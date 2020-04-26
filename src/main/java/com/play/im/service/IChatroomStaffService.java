package com.play.im.service;

import com.play.base.exception.ServiceException;
import com.play.base.service.IBaseService;
import com.play.im.model.ChatroomStaff;
import com.play.im.view.ChatroomStaffVO;

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

    /**
     * 添加聊天室工作人员
     * @param uid
     * @param roomId
     * @param userId
     * @param type 用户类型 1：房主 2：主持 3：管理
     */
    void addStaff(Long uid, Integer roomId, Long userId, Integer type) throws ServiceException;

    /**
     * 删除聊天室工作人员
     * @param uid
     * @param roomId
     * @param userId
     */
    void deleteStaff(Long uid, Integer roomId, Long userId) throws ServiceException;

    /**
     * 获取聊天室工作人员列表
     * @param roomId
     * @param type
     * @return
     */
    List<ChatroomStaffVO> list(Long roomId, Integer type);
}
