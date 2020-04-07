package com.play.im.dao;


import com.play.base.dao.IBaseDao;
import com.play.im.model.ChatroomStaff;

/**
 * Created by hushengmeng on 2017/7/4.
 */
public interface IChatroomStaffDao extends IBaseDao<ChatroomStaff> {
    /**
     * 根据房间id和用户id删除数据
     * @param roomId
     * @param userId
     */
    void deleteByRoomIdAndUserId(Integer roomId,Long userId);
}
