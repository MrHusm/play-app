package com.play.im.dao.impl;

import com.play.base.dao.impl.BaseDaoImpl;
import com.play.im.dao.IChatroomStaffDao;
import com.play.im.model.ChatroomStaff;
import org.springframework.stereotype.Repository;

/**
 * Created by hushengmeng on 2017/7/4.
 */
@Repository(value="chatroomStaffDao")
public class ChatroomStaffDaoImpl extends BaseDaoImpl<ChatroomStaff> implements IChatroomStaffDao {
}
