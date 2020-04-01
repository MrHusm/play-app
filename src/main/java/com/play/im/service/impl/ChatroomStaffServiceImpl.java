package com.play.im.service.impl;

import com.play.base.dao.IBaseDao;
import com.play.base.service.impl.BaseServiceImpl;
import com.play.im.dao.IChatroomStaffDao;
import com.play.im.model.ChatroomStaff;
import com.play.im.service.IChatroomStaffService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by lenovo on 2020/4/1.
 */
@Service(value="chatroomStaffService")
public class ChatroomStaffServiceImpl extends BaseServiceImpl<ChatroomStaff, Long> implements IChatroomStaffService {
    @Resource
    private IChatroomStaffDao chatroomStaffDao;
    @Resource(name = "redisTemplate")
    private RedisTemplate<String, ChatroomStaff> userRedisTemplate;

    @Override
    public IBaseDao<ChatroomStaff> getBaseDao() {
        return chatroomStaffDao;
    }
}