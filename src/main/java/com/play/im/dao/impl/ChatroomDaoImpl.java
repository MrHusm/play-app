package com.play.im.dao.impl;

import com.play.base.dao.impl.BaseDaoImpl;
import com.play.im.dao.IChatroomDao;
import com.play.im.model.Chatroom;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by hushengmeng on 2017/7/4.
 */
@Repository(value="chatroomDao")
public class ChatroomDaoImpl extends BaseDaoImpl<Chatroom> implements IChatroomDao {
}
