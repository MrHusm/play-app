package com.play.im.service.impl;

import com.play.base.contants.RedisKeyConstants;
import com.play.base.dao.IBaseDao;
import com.play.base.service.impl.BaseServiceImpl;
import com.play.im.dao.IChatroomStaffDao;
import com.play.im.model.ChatroomStaff;
import com.play.im.service.IChatroomStaffService;
import com.play.im.view.ChatroomStaffVO;
import com.play.ucenter.service.IUserService;
import com.play.ucenter.view.UserVO;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by lenovo on 2020/4/1.
 */
@Service(value="chatroomStaffService")
public class ChatroomStaffServiceImpl extends BaseServiceImpl<ChatroomStaff, Long> implements IChatroomStaffService {
    @Resource
    private IChatroomStaffDao chatroomStaffDao;
    @Autowired
    private IUserService userService;
    @Resource(name = "redisTemplate")
    private RedisTemplate<String, ChatroomStaffVO> redisTemplate;
    @Resource(name = "redisTemplate")
    private RedisTemplate<String, ChatroomStaff> staffRedisTemplate;

    @Override
    public IBaseDao<ChatroomStaff> getBaseDao() {
        return chatroomStaffDao;
    }

    @Override
    public List<Integer> getRoomUserRole(Integer roomId, Long userId) {
        String key = String.format(RedisKeyConstants.CACHE_CHATROOM_STAFF_KEY, roomId);
        List<Integer> roles = new ArrayList<Integer>();
        List<ChatroomStaff> staffs = staffRedisTemplate.opsForList().range(key, 0, -1);
        if (staffs == null) {
            staffs = this.findListByParams("roomId", roomId);
            for (ChatroomStaff staff : staffs) {
                staffRedisTemplate.opsForList().leftPush(key, staff);
                if (userId.longValue() == staff.getUserId().longValue()) {
                    roles.add(staff.getType());
                }
            }
        } else {
            for (ChatroomStaff staff : staffs) {
                if (staff.getUserId().longValue() == userId.longValue()) {
                    roles.add(staff.getType().intValue());
                }
            }
        }
        return roles;
    }

    @Override
    public void addStaff(Long uid,Integer roomId, Long userId, Integer type) {
        ChatroomStaff chatroomStaff = new ChatroomStaff();
        chatroomStaff.setUserId(userId);
        chatroomStaff.setRoomId(roomId);
        chatroomStaff.setType(type);
        chatroomStaff.setUpdateDate(new Date());
        chatroomStaff.setUpdateDate(new Date());
        this.save(chatroomStaff);
        String key =String.format(RedisKeyConstants.CACHE_CHATROOM_STAFF_TYPE_KEY,roomId,type);
        redisTemplate.delete(key);
    }

    @Override
    public void deleteStaff(Long uid, Integer roomId, Long userId) {
        this.chatroomStaffDao.deleteByRoomIdAndUserId(roomId,userId);
        String hostKey =String.format(RedisKeyConstants.CACHE_CHATROOM_STAFF_TYPE_KEY,roomId,2);
        String adminKey =String.format(RedisKeyConstants.CACHE_CHATROOM_STAFF_TYPE_KEY,roomId,3);
        redisTemplate.delete(adminKey);
        redisTemplate.delete(hostKey);
    }

    @Override
    public List<ChatroomStaffVO> list(Long roomId, Integer type) {
        String key =String.format(RedisKeyConstants.CACHE_CHATROOM_STAFF_TYPE_KEY,roomId,type);
        List<ChatroomStaffVO> list = redisTemplate.opsForList().range(key,0,-1);
        if(list == null){
            List<ChatroomStaff> chatroomStaffs = this.findListByParams("roomId",roomId,"type",type);
            if(CollectionUtils.isNotEmpty(chatroomStaffs)){
                for(ChatroomStaff chatroomStaff : chatroomStaffs){
                    UserVO user = this.userService.getByUserId(chatroomStaff.getUserId(),chatroomStaff.getUserId());
                    ChatroomStaffVO chatroomStaffVO = new ChatroomStaffVO();
                    chatroomStaffVO.setId(chatroomStaff.getId());
                    chatroomStaffVO.setRoomId(chatroomStaff.getRoomId());
                    chatroomStaffVO.setUserId(user.getUserId());
                    chatroomStaffVO.setPrettyId(user.getPrettyId());
                    chatroomStaffVO.setNickName(user.getNickName());
                    chatroomStaffVO.setHeadUrl(user.getHeadUrl());
                    list.add(chatroomStaffVO);
                    redisTemplate.opsForList().leftPush(key,chatroomStaffVO);
                }
                redisTemplate.expire(key,5, TimeUnit.HOURS);
            }
        }
        return list;
    }
}