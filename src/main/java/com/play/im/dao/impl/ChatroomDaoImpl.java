package com.play.im.dao.impl;

import com.play.base.dao.impl.BaseDaoImpl;
import com.play.im.dao.IChatroomDao;
import com.play.im.model.Chatroom;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hushengmeng on 2017/7/4.
 */
@Repository(value="chatroomDao")
public class ChatroomDaoImpl extends BaseDaoImpl<Chatroom> implements IChatroomDao {
    @Override
    public void updateRoomStatus(Integer roomId, Integer status) {
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("status",status);
        this.getSqlSessionTemplate().update("ChatroomMapper.updateRoomStatus",param);
    }
}
