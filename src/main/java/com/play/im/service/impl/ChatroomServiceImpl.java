package com.play.im.service.impl;

import com.play.base.contants.RedisKeyConstants;
import com.play.base.dao.IBaseDao;
import com.play.base.exception.ServiceException;
import com.play.base.service.impl.BaseServiceImpl;
import com.play.base.utils.BeanUtils;
import com.play.base.utils.ResultCustomMessage;
import com.play.im.dao.IChatroomDao;
import com.play.im.model.Chatroom;
import com.play.im.model.ChatroomStaff;
import com.play.im.service.IChatroomService;
import com.play.im.service.IChatroomStaffService;
import com.play.im.view.ChatroomVO;
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

/**
 * Created by lenovo on 2020/4/1.
 */
@Service(value="chatroomService")
public class ChatroomServiceImpl extends BaseServiceImpl<Chatroom, Long> implements IChatroomService {
    @Resource
    private IChatroomDao chatroomDao;
    @Autowired
    private IUserService userService;
    @Autowired
    private IChatroomStaffService chatroomStaffService;

    @Resource(name = "redisTemplate")
    private RedisTemplate<String, Chatroom> chatroomRedisTemplate;

    @Override
    public IBaseDao<Chatroom> getBaseDao() {
        return chatroomDao;
    }

    @Override
    public ChatroomVO getByRoomId(Integer roomId) {
        String key = String.format(RedisKeyConstants.CACHE_CHATROOM_ID_KEY,roomId);
        Chatroom chatroom = chatroomRedisTemplate.opsForValue().get(key);
        if(chatroom == null){
            chatroom = this.findUniqueByParams("roomId",roomId);
            if(chatroom != null){
                chatroomRedisTemplate.opsForValue().set(key,chatroom,1, TimeUnit.HOURS);
            }
        }
        ChatroomVO chatroomVO = BeanUtils.copyProperties(ChatroomVO.class,chatroom);
        return chatroomVO;
    }

    @Override
    public Integer createChatroom(Long userId, String name, Integer tagType)  throws ServiceException {
        //查询个人厅数量
        List<Chatroom> privateList = this.findListByParams("userId", userId);
        if (privateList != null && privateList.size() > 0) {
            throw new ServiceException(ResultCustomMessage.F1005);
        }
        Integer roomId = 1;
        UserVO user = userService.getByUserId(userId,userId);
        Chatroom chatroom = new Chatroom();
        chatroom.setRoomId(roomId);
        chatroom.setName(name);
        chatroom.setType(1);
        chatroom.setImgUrl(user.getHeadUrl());
        chatroom.setBgImgUrl("");
        chatroom.setStatus(1);
        chatroom.setOwnnerId(userId);
        chatroom.setDisplayHeart(1);
        chatroom.setTagType(tagType);
        chatroom.setMicType(1);
        chatroom.setCreateDate(new Date());
        chatroom.setUpdateDate(new Date());
        this.save(chatroom);

        ChatroomStaff chatroomStaff = new ChatroomStaff();
        chatroomStaff.setRoomId(chatroom.getRoomId());
        chatroomStaff.setUserId(userId);
        chatroomStaff.setType(1);
        chatroomStaff.setCreateDate(new Date());
        chatroomStaff.setUpdateDate(new Date());
        chatroomStaffService.save(chatroomStaff);
        return roomId;
    }

    @Override
    public List<ChatroomVO> getMineList(Long userId) {
        List<ChatroomStaff>  chatroomStaffs = this.chatroomStaffService.findListByParams("userId",userId);
        List<ChatroomVO> list = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(chatroomStaffs)){
            for(ChatroomStaff chatroomStaff : chatroomStaffs){
                Integer roomId = chatroomStaff.getRoomId();
                ChatroomVO chatroomVO = getByRoomId(roomId);
                list.add(chatroomVO);
            }
        }
        return list;
    }
}