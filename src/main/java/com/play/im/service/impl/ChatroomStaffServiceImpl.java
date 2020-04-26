package com.play.im.service.impl;

import com.alibaba.fastjson.JSON;
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
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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
    private RedisTemplate<String, String> stringRedisTemplate;

    @Override
    public IBaseDao<ChatroomStaff> getBaseDao() {
        return chatroomStaffDao;
    }

    @Override
    public List<Integer> getRoomUserRole(Integer roomId, Long userId) {
        String key = String.format(RedisKeyConstants.CACHE_CHATROOM_STAFF_KEY, roomId);
        List<Integer> roles = new ArrayList<Integer>();
        List<String> staffJsons = stringRedisTemplate.opsForList().range(key, 0, -1);
        List<ChatroomStaff> staffs = null;
        if (CollectionUtils.isEmpty(staffJsons)) {
            staffs = this.findListByParams("roomId", roomId);
            for (ChatroomStaff staff : staffs) {
                stringRedisTemplate.opsForList().leftPush(key, JSON.toJSONString(staff));
                if (userId.longValue() == staff.getUserId().longValue()) {
                    roles.add(staff.getType());
                }
            }
        } else {
            for (String staffJson : staffJsons) {
                ChatroomStaff staff = JSON.parseObject(staffJson, ChatroomStaff.class);
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
        stringRedisTemplate.delete(key);
    }

    @Override
    public void deleteStaff(Long uid, Integer roomId, Long userId) {
        this.chatroomStaffDao.deleteByRoomIdAndUserId(roomId,userId);
        String hostKey =String.format(RedisKeyConstants.CACHE_CHATROOM_STAFF_TYPE_KEY,roomId,2);
        String adminKey =String.format(RedisKeyConstants.CACHE_CHATROOM_STAFF_TYPE_KEY,roomId,3);
        stringRedisTemplate.delete(adminKey);
        stringRedisTemplate.delete(hostKey);
    }

    @Override
    public List<ChatroomStaffVO> list(Long roomId, Integer type) {
        String key =String.format(RedisKeyConstants.CACHE_CHATROOM_STAFF_TYPE_KEY,roomId,type);
        List<String> staffJsons = stringRedisTemplate.opsForList().range(key, 0, -1);
        List<ChatroomStaffVO> list = null;
        if (CollectionUtils.isEmpty(staffJsons)) {
            List<ChatroomStaff> chatroomStaffs = this.findListByParams("roomId",roomId,"type",type);
            if(CollectionUtils.isNotEmpty(chatroomStaffs)){
                list = new ArrayList<>();
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
                    stringRedisTemplate.opsForList().leftPush(key, JSON.toJSONString(chatroomStaffVO));
                }
                stringRedisTemplate.expire(key, 5, TimeUnit.HOURS);
            }
        } else {
            list = staffJsons.stream().map(staffJson -> JSON.parseObject(staffJson, ChatroomStaffVO.class)).collect(Collectors.toList());
        }
        return list;
    }
}