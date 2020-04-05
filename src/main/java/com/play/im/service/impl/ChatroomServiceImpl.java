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
import com.play.im.view.RoomMicVO;
import com.play.ucenter.service.IUserService;
import com.play.ucenter.view.UserMicVO;
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
    @Resource(name = "redisTemplate")
    private RedisTemplate<String, Long> redisTemplate;
    @Resource(name = "redisTemplate")
    private RedisTemplate<String, UserMicVO> userMicRedisTemplate;
    @Resource(name = "redisTemplate")
    private RedisTemplate<String, RoomMicVO> roomMicRedisTemplate;

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
        List<ChatroomVO> list = new ArrayList<ChatroomVO>();
        if(CollectionUtils.isNotEmpty(chatroomStaffs)){
            for(ChatroomStaff chatroomStaff : chatroomStaffs){
                Integer roomId = chatroomStaff.getRoomId();
                ChatroomVO chatroomVO = getByRoomId(roomId);
                list.add(chatroomVO);
            }
        }
        return list;
    }

    @Override
    public void join(Long userId, Integer roomId, Integer pwd) throws ServiceException {
        //查看用户是否在房间黑名单中
        String blackKey = String.format(RedisKeyConstants.CACHE_CHATROOM_BLACK_KEY, roomId);
        List<Long> blackList = redisTemplate.opsForList().range(blackKey, 0, -1);
        for (Long blackUid : blackList) {
            if (blackUid.longValue() == userId.longValue()) {
                throw new ServiceException(ResultCustomMessage.F1006);
            }
        }
        ChatroomVO chatroomVO = this.getByRoomId(roomId);
        //角色类型1：房主 2：主持 3：管理
        List<Integer> roomUserRole = chatroomStaffService.getRoomUserRole(roomId, userId);
        //1：开启 2：关闭 3：冻结
        Integer roomStatus = chatroomVO.getStatus();
        if (roomStatus == 3) {
            //聊天室被冻结
            throw new ServiceException(ResultCustomMessage.F1007);
        } else if (roomStatus == 2) {
            //聊天室关闭 游客禁止进入
            if (!(roomUserRole.contains(1) || roomUserRole.contains(2) || roomUserRole.contains(3))) {
                throw new ServiceException(ResultCustomMessage.F1008);
            }
        }
        //聊天室加锁
        String encryptKey = RedisKeyConstants.CACHE_CHATROOM_ENCRYPT_KEY;
        Double password = redisTemplate.opsForZSet().score(encryptKey, userId);
        if (password != null) {
            //聊天室加锁 游客需要进入
            if (!(roomUserRole.contains(1) || roomUserRole.contains(2) || roomUserRole.contains(3))) {
                if (pwd == null || password.intValue() != pwd.intValue()) {
                    throw new ServiceException(ResultCustomMessage.F1009);
                }
            }
        }
        //获取排麦用户队列
        String micKey = String.format(RedisKeyConstants.CACHE_CHATROOM_MIC_QUEUE_KEY, roomId);
        List<UserMicVO> userMicVOS = userMicRedisTemplate.opsForList().range(micKey, 0, -1);

        //进入到聊天室 缓存数据 获取声网token  channelName
//        AgoraChannelDto agoraChannelDto = agoraService.getChatroomAgoraChannel(userId, roomId, flag);

        //聊天室热度值
//        Integer hotValue = roomGiftService.getChatroomActive(roomId);
//        Integer nobilityScore = 0;
//
//        //未隐身状态加爵位人气
//        if (!hideRoomOnline) {
//            nobilityScore = getJoinScoreByNobility(loginUserInfoDto.getNobilityInfo());
//            hotValue += nobilityScore + 10;
//        }
//
//        chatroomInfo.setOnlineUserCount(hotValue);
//
//        // 公屏消息状态 1：显示 2：隐藏
//        Integer msgStatus = chatRoomCache.getMsgStatus(roomId);
//        chatroomInfo.setMsgStatus(msgStatus);

        //进入房间(异步)
        this.joinRoom(roomId, userId);
        //获取麦位信息
        List<RoomMicVO> roomMicVOS = getRoomMicInfo(roomId);

//        if (type == 0 && !(roomUserRole.contains(UserRoleEnum.ANONYM.getRole())) && !hideRoomOnline) {
//            //第一次进入发送融云消息：某人进入房间，浮窗模式进入不发送融云消息
//            messageChatroomService.publishJoinOrLeaveChatroomV2(roomId, userId, "joinRoom", loginUserInfoDto, hotValue, userId + "进入房间");
//        }

    }

    @Override
    public void upMic(Long userId, Long micUserId, Integer roomId, Integer position) throws ServiceException {
        ChatroomVO chatroomVO = this.getByRoomId(roomId);
        UserVO user = this.userService.getByUserId(micUserId, micUserId);
        if (chatroomVO.getMicType() == 1 && userId.intValue() == micUserId) {
            //审批上麦 非主持人操作 加入排麦队列
            UserMicVO userMicVO = BeanUtils.copyProperties(UserMicVO.class, user);
            userMicVO.setPosition(position);
            String micKey = String.format(RedisKeyConstants.CACHE_CHATROOM_MIC_QUEUE_KEY, roomId);
            userMicRedisTemplate.opsForList().leftPush(micKey, userMicVO);
        } else {
            //自由上麦 或者主持人操作
            String roomMicKey = String.format(RedisKeyConstants.CACHE_CHATROOM_MIC_KEY, roomId);
            RoomMicVO roomMicVO = roomMicRedisTemplate.opsForList().index(roomMicKey, position);
            if (roomMicVO.getUserId() != null) {
                throw new ServiceException(ResultCustomMessage.F1010);
            }
            roomMicVO.setUserId(user.getUserId());
            roomMicVO.setPrettyId(user.getPrettyId());
            roomMicVO.setNickName(user.getNickName());
            roomMicVO.setHeadUrl(user.getHeadUrl());
            roomMicRedisTemplate.opsForList().set(roomMicKey, position, roomMicVO);
        }
    }

    @Override
    public void downMic(Long micUserId, Integer roomId) {
        String roomMicKey = String.format(RedisKeyConstants.CACHE_CHATROOM_MIC_KEY, roomId);
        List<RoomMicVO> roomMicVOS = roomMicRedisTemplate.opsForList().range(roomMicKey, 0, -1);
        for (int i = 0; i < roomMicVOS.size(); i++) {
            RoomMicVO roomMicVO = roomMicVOS.get(i);
            if (roomMicVO.getUserId() != null && roomMicVO.getUserId().intValue() == micUserId.intValue()) {
                roomMicVO.setUserId(null);
                roomMicVO.setPrettyId(null);
                roomMicVO.setNickName(null);
                roomMicVO.setHeadUrl(null);
                roomMicRedisTemplate.opsForList().set(roomMicKey, i, roomMicVO);
                //发送下麦消息 TODO

            }
        }
    }

    @Override
    public void cancelUpMic(Long micUserId, Integer roomId) {
        String micKey = String.format(RedisKeyConstants.CACHE_CHATROOM_MIC_QUEUE_KEY, roomId);
        List<UserMicVO> userMicVOS = userMicRedisTemplate.opsForList().range(micKey, 0, -1);
        for (UserMicVO userMicVO : userMicVOS) {
            if (userMicVO.getUserId().longValue() == micUserId) {
                userMicRedisTemplate.opsForList().remove(micKey, 0, userMicVO);
                //发送取消排麦消息 TODO
                break;
            }
        }
    }

    @Override
    public List<UserMicVO> roomMicQueue(Integer roomId) {
        String micKey = String.format(RedisKeyConstants.CACHE_CHATROOM_MIC_QUEUE_KEY, roomId);
        List<UserMicVO> userMicVOS = userMicRedisTemplate.opsForList().range(micKey, 0, -1);
        return userMicVOS;
    }

    /**
     * 获取房间麦位信息
     *
     * @param roomId
     */
    private List<RoomMicVO> getRoomMicInfo(Integer roomId) {
        String roomMicKey = String.format(RedisKeyConstants.CACHE_CHATROOM_MIC_KEY, roomId);
        List<RoomMicVO> roomMicVOS = roomMicRedisTemplate.opsForList().range(roomMicKey, 0, -1);
        if (CollectionUtils.isEmpty(roomMicVOS)) {
            //初始化麦位信息
            for (int i = 0; i < 9; i++) {
                RoomMicVO roomMicVO = new RoomMicVO();
                roomMicRedisTemplate.opsForList().leftPush(roomMicKey, roomMicVO);
                roomMicVOS.add(roomMicVO);
            }
        }
        return roomMicVOS;
    }

    /**
     * 加入房间
     *
     * @param roomId
     * @param userId
     */
    private void joinRoom(Integer roomId, Long userId) {
        //用户所在房间key
        String userChatroomKey = RedisKeyConstants.CACHE_USER_CHATROOM_KEY;
        Object oldRoomId = redisTemplate.opsForHash().get(userChatroomKey, userId);
        if (oldRoomId != null && Integer.parseInt(oldRoomId.toString()) != roomId.intValue()) {
            //删除原所在房间的用户信息
            String oldRoomUserKey = String.format(RedisKeyConstants.CACHE_CHATROOM_USER_KEY, Integer.parseInt(oldRoomId.toString()));
            redisTemplate.opsForZSet().remove(oldRoomUserKey, userId);
            //原所在房间用户取消排麦
            cancelUpMic(userId, Integer.parseInt(oldRoomId.toString()));
            //原所在房间用户下麦
            downMic(userId, Integer.parseInt(oldRoomId.toString()));
        }
        //添加用户到房间用户列表中
        String roomUserKey = String.format(RedisKeyConstants.CACHE_CHATROOM_USER_KEY, roomId);
        redisTemplate.opsForZSet().add(roomUserKey, userId, System.currentTimeMillis());
        //记录用户所在房间
        redisTemplate.opsForHash().put(userChatroomKey, userId, roomId);

    }
}