package com.play.im.service.impl;

import com.play.base.contants.RedisKeyConstants;
import com.play.base.dao.IBaseDao;
import com.play.base.service.impl.BaseServiceImpl;
import com.play.im.dao.IChatroomStaffDao;
import com.play.im.model.ChatroomStaff;
import com.play.im.service.IChatroomStaffService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by lenovo on 2020/4/1.
 */
@Service(value="chatroomStaffService")
public class ChatroomStaffServiceImpl extends BaseServiceImpl<ChatroomStaff, Long> implements IChatroomStaffService {
    @Resource
    private IChatroomStaffDao chatroomStaffDao;
    @Resource(name = "redisTemplate")
    private RedisTemplate<String, Long> redisTemplate;

    @Override
    public IBaseDao<ChatroomStaff> getBaseDao() {
        return chatroomStaffDao;
    }

    @Override
    public List<Integer> getRoomUserRole(Integer roomId, Long userId) {
        String key = String.format(RedisKeyConstants.CACHE_CHATROOM_STAFF_KEY, roomId);
        List<Integer> roles = new ArrayList<Integer>();
        Set<ZSetOperations.TypedTuple<Long>> staffs = redisTemplate.opsForZSet().rangeWithScores(key, 0, -1);
        if (staffs == null) {
            List<ChatroomStaff> chatroomStaffs = this.findListByParams("roomId", roomId);
            for (ChatroomStaff staff : chatroomStaffs) {
                redisTemplate.opsForZSet().add(key, staff.getUserId(), staff.getType());
                if (userId.longValue() == staff.getUserId().longValue()) {
                    roles.add(staff.getType());
                }
            }
        } else {
            for (ZSetOperations.TypedTuple<Long> staff : staffs) {
                if (staff.getValue().longValue() == userId.longValue()) {
                    roles.add(staff.getScore().intValue());
                }
            }
        }
        return roles;
    }
}