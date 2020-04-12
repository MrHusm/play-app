package com.play.im.dao.impl;

import com.play.base.dao.impl.BaseDaoImpl;
import com.play.im.dao.IChatroomStaffDao;
import com.play.im.model.ChatroomStaff;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hushengmeng on 2017/7/4.
 */
@Repository(value="chatroomStaffDao")
public class ChatroomStaffDaoImpl extends BaseDaoImpl<ChatroomStaff> implements IChatroomStaffDao {
    @Override
    public void deleteByRoomIdAndUserId(Integer roomId, Long userId) {
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("roomId",roomId);
        param.put("userId",userId);
        delete("ChatroomStaffMapper.deleteByRoomIdAndUserId",param);
    }
}
