package com.play.im.service.impl;

import com.alibaba.fastjson.JSON;
import com.play.base.contants.RedisKeyConstants;
import com.play.base.dao.IBaseDao;
import com.play.base.exception.ServiceException;
import com.play.base.service.impl.BaseServiceImpl;
import com.play.base.utils.*;
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
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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
    private RedisTemplate<String, Long> longRedisTemplate;
    @Resource(name = "redisTemplate")
    private RedisTemplate<String, Integer> intRedisTemplate;
    @Resource(name = "redisTemplate")
    private RedisTemplate<String, String> stringRedisTemplate;

    @Override
    public IBaseDao<Chatroom> getBaseDao() {
        return chatroomDao;
    }

    private static Set<Long> adminUserIds = new HashSet<>();

    static {
        adminUserIds.add(66666666L);
        adminUserIds.add(88888888L);
    }

    @Override
    public ChatroomVO getByRoomId(Integer roomId) {
        String key = String.format(RedisKeyConstants.CACHE_CHATROOM_ID_KEY,roomId);
        String chatroomJson = stringRedisTemplate.opsForValue().get(key);
        Chatroom chatroom = null;
        if(chatroomJson == null){
            chatroom = this.findUniqueByParams("roomId",roomId);
            if(chatroom != null){
                stringRedisTemplate.opsForValue().set(key, JSON.toJSONString(chatroom),3, TimeUnit.HOURS);
            }
        }else{
            chatroom = JSON.parseObject(chatroomJson,Chatroom.class);
        }
        ChatroomVO chatroomVO = BeanUtils.copyProperties(ChatroomVO.class,chatroom);
        return chatroomVO;
    }

    @Transactional
    @Override
    public Integer createChatroom(Long userId, String name, Integer tagType)  throws ServiceException {
        //查询个人厅数量
        List<Chatroom> privateList = this.findListByParams("userId", userId);
        if (privateList != null && privateList.size() > 0) {
            throw new ServiceException(ResultCustomMessage.F1005);
        }
        String key = RedisKeyConstants.CACHE_ROOM_CODE_KEY;
        BoundSetOperations<String, Long> operations = longRedisTemplate.boundSetOps(key);
        Number roomId = operations.randomMember();
        operations.remove(userId);
        UserVO user = userService.getByUserId(userId,userId);
        Chatroom chatroom = new Chatroom();
        chatroom.setRoomId(roomId.intValue());
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
        return roomId.intValue();
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
    public Map<String, Object> getRoomInfo(Long userId, Integer roomId, Integer pwd) throws ServiceException {
        //查看用户是否在房间黑名单中
        String blackKey = String.format(RedisKeyConstants.CACHE_CHATROOM_BLACK_KEY, roomId);
        List<Long> blackList = longRedisTemplate.opsForList().range(blackKey, 0, -1);
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
        String encryptKey = RedisKeyConstants.CACHE_CHATROOM_LOCK_KEY;
        Double password = longRedisTemplate.opsForZSet().score(encryptKey, roomId);
        if (password != null) {
            //聊天室加锁 游客需要进入
            if (!(roomUserRole.contains(1) || roomUserRole.contains(2) || roomUserRole.contains(3))) {
                if (pwd == null || password.intValue() != pwd.intValue()) {
                    throw new ServiceException(ResultCustomMessage.F1009);
                }
            }
        }
        Map<String, Object> result = new HashMap<>();
        result.put("chatroom", chatroomVO);
        //获取排麦用户队列
        String micKey = String.format(RedisKeyConstants.CACHE_CHATROOM_MIC_QUEUE_KEY, roomId);
        List<String> userMicJsons = stringRedisTemplate.opsForList().range(micKey, 0, -1);
        List<UserMicVO> userMicVOS = userMicJsons.stream().map(userMicJson -> JSON.parseObject(userMicJson, UserMicVO.class)).collect(Collectors.toList());
        result.put("userMics", userMicVOS);

        //进入到聊天室 缓存数据 获取声网token  channelName TODO
//        AgoraChannelDto agoraChannelDto = agoraService.getChatroomAgoraChannel(userId, roomId, flag);
        return result;
    }

    /**
     * 获取房间麦位信息
     *
     * @param roomId
     */
    @Async
    @Override
    public Future<List<RoomMicVO>> getRoomMicInfo(Integer roomId) {
        String roomMicKey = String.format(RedisKeyConstants.CACHE_CHATROOM_MIC_KEY, roomId);
        List<String> roomMicJsons = stringRedisTemplate.opsForList().range(roomMicKey, 0, -1);
        List<RoomMicVO> roomMicVOS = roomMicJsons.stream().map(roomMicJson -> JSON.parseObject(roomMicJson, RoomMicVO.class)).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(roomMicVOS)) {
            //初始化麦位信息
            for (int i = 0; i < 9; i++) {
                RoomMicVO roomMicVO = new RoomMicVO();
                stringRedisTemplate.opsForList().leftPush(roomMicKey, JSON.toJSONString(roomMicVO));
                roomMicVOS.add(roomMicVO);
            }
        }
        Long now = System.currentTimeMillis();
        List<Object> hks = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8);
        //获取麦位心动值
        String heartKey = String.format(RedisKeyConstants.CACHE_CHATROOM_MIC_HEART_KEY, roomId);
        List<Object> heartValues = intRedisTemplate.opsForHash().multiGet(heartKey, hks);
        //获取麦位状态
        String micStatusKey = String.format(RedisKeyConstants.CACHE_CHATROOM_MIC_STATUS_KEY, roomId);
        List<Object> micStatusValues = intRedisTemplate.opsForHash().multiGet(micStatusKey, hks);
        //获取麦位倒计时
        List<String> timerKeys = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            String timerKey = String.format(RedisKeyConstants.CACHE_CHATROOM_TIMER_POSITION_KEY, roomId, i);
            timerKeys.add(timerKey);
        }
        List<Long> timers = longRedisTemplate.opsForValue().multiGet(timerKeys);
        for (int i = 0; i < roomMicVOS.size(); i++) {
            RoomMicVO roomMicVO = roomMicVOS.get(i);
            //麦位心动值
            Object heartValue = heartValues.get(i);
            if (heartValue != null) {
                roomMicVO.setHeartValue(Integer.parseInt(heartValue.toString()));
            } else {
                roomMicVO.setHeartValue(0);
            }
            //麦位倒计时
            Long timer = timers.get(i);
            if (timer != null) {
                Long endTime = (timer - now) / 1000;
                roomMicVO.setEndTime(endTime.intValue());
            } else {
                roomMicVO.setEndTime(0);
            }
            //麦位状态
            Object micStatusValue = micStatusValues.get(i);
            if (micStatusValue != null) {
                roomMicVO.setMicStatus(1);
            } else {
                roomMicVO.setMicStatus(0);
            }
        }
        return new AsyncResult<List<RoomMicVO>>(roomMicVOS);
    }

    /**
     * 加入房间
     *
     * @param roomId
     * @param userId
     */
    @Async
    @Override
    public void joinRoom(Integer roomId, Long userId) {
        //用户所在房间key
        String userChatroomKey = RedisKeyConstants.CACHE_USER_CHATROOM_KEY;
        Object oldRoomId = longRedisTemplate.opsForHash().get(userChatroomKey, userId);
        if (oldRoomId != null && Integer.parseInt(oldRoomId.toString()) != roomId.intValue()) {
            //离开原来房间
            leave(Integer.parseInt(oldRoomId.toString()), userId);
        }
        if (!adminUserIds.contains(userId)) {
            //添加用户到房间用户列表中
            String roomUserKey = String.format(RedisKeyConstants.CACHE_CHATROOM_USER_KEY, roomId);
            longRedisTemplate.opsForZSet().add(roomUserKey, userId, System.currentTimeMillis());
            //记录用户所在房间
            longRedisTemplate.opsForHash().put(userChatroomKey, userId, roomId);
        }
        //发送聊天消息 TODO
        if (!adminUserIds.contains(userId)) {
            //messageChatroomService.publishJoinOrLeaveChatroomV2(roomId, userId, "joinRoom", loginUserInfoDto, hotValue, userId + "进入房间");
        }
    }


    @Override
    public void upMic(Long userId, Long micUserId, Integer roomId, Integer position) throws ServiceException {
        ChatroomVO chatroomVO = this.getByRoomId(roomId);
        UserVO user = this.userService.getByUserId(micUserId, micUserId);
        //角色类型1：房主 2：主持 3：管理
        List<Integer> roomUserRole = chatroomStaffService.getRoomUserRole(roomId, userId);
        if (roomUserRole.contains(1) || roomUserRole.contains(2) || roomUserRole.contains(3) || chatroomVO.getMicType() == 2) {
            //自由上麦 或者主持人、管理、房主操作
            String roomMicKey = String.format(RedisKeyConstants.CACHE_CHATROOM_MIC_KEY, roomId);
            RoomMicVO roomMicVO = JSON.parseObject(stringRedisTemplate.opsForList().index(roomMicKey, position), RoomMicVO.class);
            if (roomMicVO.getUserId() != null) {
                throw new ServiceException(ResultCustomMessage.F1010);
            }
            //下麦
            downMic(micUserId, roomId);
            //上麦
            roomMicVO.setUserId(user.getUserId());
            roomMicVO.setPrettyId(user.getPrettyId());
            roomMicVO.setNickName(user.getNickName());
            roomMicVO.setHeadUrl(user.getHeadUrl());
            stringRedisTemplate.opsForList().set(roomMicKey, position, JSON.toJSONString(roomMicVO));
            //发送上麦消息 TODO
        } else {
            //审批上麦
            UserMicVO userMicVO = BeanUtils.copyProperties(UserMicVO.class, user);
            userMicVO.setPosition(position);
            String micKey = String.format(RedisKeyConstants.CACHE_CHATROOM_MIC_QUEUE_KEY, roomId);
            stringRedisTemplate.opsForList().leftPush(micKey, JSON.toJSONString(userMicVO));
            //发送排麦消息 TODO
        }
    }

    @Override
    public void downMic(Long micUserId, Integer roomId) {
        String roomMicKey = String.format(RedisKeyConstants.CACHE_CHATROOM_MIC_KEY, roomId);
        List<String> roomMicJsons = stringRedisTemplate.opsForList().range(roomMicKey, 0, -1);
        List<RoomMicVO> roomMicVOS = roomMicJsons.stream().map(roomMicJson -> JSON.parseObject(roomMicJson, RoomMicVO.class)).collect(Collectors.toList());
        for (int i = 0; i < roomMicVOS.size(); i++) {
            RoomMicVO roomMicVO = roomMicVOS.get(i);
            if (roomMicVO.getUserId() != null && roomMicVO.getUserId().intValue() == micUserId.intValue()) {
                roomMicVO.setUserId(null);
                roomMicVO.setPrettyId(null);
                roomMicVO.setNickName(null);
                roomMicVO.setHeadUrl(null);
                stringRedisTemplate.opsForList().set(roomMicKey, i, JSON.toJSONString(roomMicVO));
                //发送下麦消息 TODO

            }
        }
    }

    @Override
    public void cancelUpMic(Long micUserId, Integer roomId) {
        String micKey = String.format(RedisKeyConstants.CACHE_CHATROOM_MIC_QUEUE_KEY, roomId);
        List<String> userMicJsons = stringRedisTemplate.opsForList().range(micKey, 0, -1);
        List<UserMicVO> userMicVOS = userMicJsons.stream().map(userMicJson -> JSON.parseObject(userMicJson, UserMicVO.class)).collect(Collectors.toList());
        for (UserMicVO userMicVO : userMicVOS) {
            if (userMicVO.getUserId().longValue() == micUserId) {
                stringRedisTemplate.opsForList().remove(micKey, 0, JSON.toJSONString(userMicVO));
                //发送取消排麦消息 TODO
                break;
            }
        }
    }

    @Override
    public List<UserMicVO> roomMicQueue(Integer roomId) {
        String micKey = String.format(RedisKeyConstants.CACHE_CHATROOM_MIC_QUEUE_KEY, roomId);
        List<String> userMicJsons = stringRedisTemplate.opsForList().range(micKey, 0, -1);
        List<UserMicVO> userMicVOS = userMicJsons.stream().map(userMicJson -> JSON.parseObject(userMicJson, UserMicVO.class)).collect(Collectors.toList());
        return userMicVOS;
    }

    @Override
    public PageFinder<UserVO> getChatroomUsersByPage(Long userId, Integer roomId, Query query) {
        //添加用户到房间用户列表中
        String roomUserKey = String.format(RedisKeyConstants.CACHE_CHATROOM_USER_KEY, roomId);
        ZSetOperations<String, Integer> operations = intRedisTemplate.opsForZSet();
        int total = operations.size(roomUserKey).intValue();
        Set<Integer> userIds = operations.reverseRange(roomUserKey, query.getOffset(), query.getOffset() + query.getPageSize() - 1);
        List<UserVO> users = new ArrayList<UserVO>();
        for (Integer uid : userIds) {
            UserVO user = this.userService.getByUserId(userId, Long.valueOf(uid));
            users.add(user);
        }
        return new PageFinder(query.getPage(), query.getPageSize(), total, users);
    }

    @Override
    public void addNospeak(Long uid, Long userId, Integer roomId, Integer time) throws ServiceException {
        //角色类型1：房主 2：主持 3：管理
        List<Integer> roomUserRole = chatroomStaffService.getRoomUserRole(roomId, uid);
        if (!(roomUserRole.contains(1) || roomUserRole.contains(2) || roomUserRole.contains(3))) {
            throw new ServiceException(ResultCustomMessage.F1015);
        }
        String key = String.format(RedisKeyConstants.CACHE_CHATROOM_USER_NOSPEAK_KEY, roomId);
        if (time == -1) {
            time = 999999;
        }
        longRedisTemplate.opsForZSet().add(key, userId, System.currentTimeMillis() + time * 60 * 1000);
    }

    @Override
    public void removeNospeak(Long uid, Long userId, Integer roomId) throws ServiceException {
        //角色类型1：房主 2：主持 3：管理
        List<Integer> roomUserRole = chatroomStaffService.getRoomUserRole(roomId, uid);
        if (!(roomUserRole.contains(1) || roomUserRole.contains(2) || roomUserRole.contains(3))) {
            throw new ServiceException(ResultCustomMessage.F1015);
        }
        String key = String.format(RedisKeyConstants.CACHE_CHATROOM_USER_NOSPEAK_KEY, roomId);
        longRedisTemplate.opsForZSet().remove(key, userId);
    }

    @Override
    public List<UserVO> nospeakList(Long userId, Integer roomId) {
        //添加用户到房间用户列表中
        String key = String.format(RedisKeyConstants.CACHE_CHATROOM_USER_NOSPEAK_KEY, roomId);
        Set<Long> userIds = longRedisTemplate.opsForZSet().rangeByScore(key, 0, -1);
        List<UserVO> users = new ArrayList<UserVO>();
        for (Long uid : userIds) {
            UserVO user = this.userService.getByUserId(userId, uid);
            users.add(user);
        }
        return users;
    }

    @Override
    public void addBlack(Long uid, Long userId, Integer roomId) throws ServiceException {
        //角色类型1：房主 2：主持 3：管理
        List<Integer> roomUserRole = chatroomStaffService.getRoomUserRole(roomId, uid);
        if (!(roomUserRole.contains(1) || roomUserRole.contains(2) || roomUserRole.contains(3))) {
            throw new ServiceException(ResultCustomMessage.F1015);
        }
        String blackKey = String.format(RedisKeyConstants.CACHE_CHATROOM_BLACK_KEY, roomId);
        longRedisTemplate.opsForList().leftPush(blackKey, userId);
    }

    @Override
    public void removeBlack(Long uid, Long userId, Integer roomId) {
        String blackKey = String.format(RedisKeyConstants.CACHE_CHATROOM_BLACK_KEY, roomId);
        longRedisTemplate.opsForList().remove(blackKey, 0, userId);
    }

    @Override
    public List<UserVO> blackList(Long userId, Integer roomId) {
        String blackKey = String.format(RedisKeyConstants.CACHE_CHATROOM_BLACK_KEY, roomId);
        List<Long> userIds = longRedisTemplate.opsForList().range(blackKey, 0, -1);
        List<UserVO> users = new ArrayList<UserVO>();
        for (Long uid : userIds) {
            UserVO user = this.userService.getByUserId(userId, uid);
            users.add(user);
        }
        return users;
    }

    @Override
    public void leave(Integer roomId, Long userId) {
        //删除房间的用户信息
        String roomUserKey = String.format(RedisKeyConstants.CACHE_CHATROOM_USER_KEY, roomId);
        longRedisTemplate.opsForZSet().remove(roomUserKey, userId);
        //删除记录用户所在的房间
        String userChatroomKey = RedisKeyConstants.CACHE_USER_CHATROOM_KEY;
        longRedisTemplate.opsForHash().delete(userChatroomKey, userId);
        //取消排麦
        cancelUpMic(userId, Integer.parseInt(roomId.toString()));
        //用户下麦
        downMic(userId, Integer.parseInt(roomId.toString()));
    }

    @Transactional
    @Override
    public void close(Long userId, Integer roomId) throws ServiceException {
        //角色类型1：房主 2：主持 3：管理
        List<Integer> roomUserRole = chatroomStaffService.getRoomUserRole(roomId, userId);
        if (!(roomUserRole.contains(1) || roomUserRole.contains(2) || roomUserRole.contains(3))) {
            throw new ServiceException(ResultCustomMessage.F1015);
        }
        String roomUserKey = String.format(RedisKeyConstants.CACHE_CHATROOM_USER_KEY, roomId);
        //删除记录用户所在的房间
        Set<Integer> userIds = intRedisTemplate.opsForZSet().range(roomUserKey, 0, -1);
        String userChatroomKey = RedisKeyConstants.CACHE_USER_CHATROOM_KEY;
        intRedisTemplate.opsForHash().delete(userChatroomKey, userIds.toArray());
        //删除房间所有用户信息
        longRedisTemplate.delete(roomUserKey);
        //取消用户排麦
        String micKey = String.format(RedisKeyConstants.CACHE_CHATROOM_MIC_QUEUE_KEY, roomId);
        longRedisTemplate.delete(micKey);
        //所有用户下麦
        String roomMicKey = String.format(RedisKeyConstants.CACHE_CHATROOM_MIC_KEY, roomId);
        longRedisTemplate.delete(roomMicKey);
        //关闭融云聊天室 TODO
        this.chatroomDao.updateRoomStatus(roomId,2);
        //删除聊天室缓存
        String key = String.format(RedisKeyConstants.CACHE_CHATROOM_ID_KEY, roomId);
        stringRedisTemplate.delete(key);
    }

    @Transactional
    @Override
    public void open(Long userId, Integer roomId) throws ServiceException {
        //角色类型1：房主 2：主持 3：管理
        List<Integer> roomUserRole = chatroomStaffService.getRoomUserRole(roomId, userId);
        if (!(roomUserRole.contains(1) || roomUserRole.contains(2) || roomUserRole.contains(3))) {
            throw new ServiceException(ResultCustomMessage.F1015);
        }
        //开启融云聊天室 TODO
        this.chatroomDao.updateRoomStatus(roomId,1);
        //删除聊天室缓存
        String key = String.format(RedisKeyConstants.CACHE_CHATROOM_ID_KEY, roomId);
        stringRedisTemplate.delete(key);
    }

    @Transactional
    @Override
    public void updateChatroom(Long userId, ChatroomVO chatroom) throws ServiceException {
        //角色类型1：房主 2：主持 3：管理
        List<Integer> roomUserRole = chatroomStaffService.getRoomUserRole(chatroom.getRoomId(), userId);
        if (!(roomUserRole.contains(1) || roomUserRole.contains(2) || roomUserRole.contains(3))) {
            throw new ServiceException(ResultCustomMessage.F1015);
        }
        Chatroom room = new Chatroom();
        room.setRoomId(chatroom.getRoomId());
        room.setName(chatroom.getName());
        room.setImgUrl(chatroom.getImgUrl());
        room.setBgImgUrl(chatroom.getBgImgUrl());
        room.setNotice(chatroom.getNotice());
        room.setSlogan(chatroom.getSlogan());
        room.setDisplayHeart(chatroom.getDisplayHeart());
        room.setMicType(chatroom.getMicType());
        room.setTagType(chatroom.getTagType());
        room.setUpdateDate(new Date());
        this.update(room);
        String key = String.format(RedisKeyConstants.CACHE_CHATROOM_ID_KEY,chatroom.getRoomId());
        longRedisTemplate.delete(key);
    }

    @Override
    public void startTimer(Long userId, Integer roomId, Integer position, Integer num) throws ServiceException {
        //角色类型1：房主 2：主持 3：管理
        List<Integer> roomUserRole = chatroomStaffService.getRoomUserRole(roomId, userId);
        if (!(roomUserRole.contains(1) || roomUserRole.contains(2) || roomUserRole.contains(3))) {
            throw new ServiceException(ResultCustomMessage.F1015);
        }
        String key = String.format(RedisKeyConstants.CACHE_CHATROOM_TIMER_POSITION_KEY,roomId,position);
        ValueOperations<String, Long> valueOperations = longRedisTemplate.opsForValue();
        long time=(num+1)*1000;
        valueOperations.set(key, System.currentTimeMillis()+time);
        longRedisTemplate.expire(key, time, TimeUnit.MILLISECONDS);
        //发送融云倒计时消息 TODO
    }

    @Override
    public void stopTimer(Long userId, Integer roomId, Integer position) throws ServiceException {
        //角色类型1：房主 2：主持 3：管理
        List<Integer> roomUserRole = chatroomStaffService.getRoomUserRole(roomId, userId);
        if (!(roomUserRole.contains(1) || roomUserRole.contains(2) || roomUserRole.contains(3))) {
            throw new ServiceException(ResultCustomMessage.F1015);
        }
        String key = String.format(RedisKeyConstants.CACHE_CHATROOM_TIMER_POSITION_KEY,roomId,position);
        longRedisTemplate.delete(key);
        //发送融云倒计时停止消息 TODO
    }

    @Override
    public void openMic(Long userId, Integer roomId, Integer position) throws ServiceException {
        //角色类型1：房主 2：主持 3：管理
        List<Integer> roomUserRole = chatroomStaffService.getRoomUserRole(roomId, userId);
        if (!(roomUserRole.contains(1) || roomUserRole.contains(2) || roomUserRole.contains(3))) {
            throw new ServiceException(ResultCustomMessage.F1015);
        }
        String key = String.format(RedisKeyConstants.CACHE_CHATROOM_MIC_STATUS_KEY, roomId);
        intRedisTemplate.opsForHash().put(key, position, 1);
        //发送融云开启麦位消息 TODO
    }

    @Override
    public void closeMic(Long userId, Integer roomId, Integer position) throws ServiceException {
        //角色类型1：房主 2：主持 3：管理
        List<Integer> roomUserRole = chatroomStaffService.getRoomUserRole(roomId, userId);
        if (!(roomUserRole.contains(1) || roomUserRole.contains(2) || roomUserRole.contains(3))) {
            throw new ServiceException(ResultCustomMessage.F1015);
        }
        String key = String.format(RedisKeyConstants.CACHE_CHATROOM_MIC_STATUS_KEY, roomId);
        intRedisTemplate.opsForHash().delete(key, position);
        //发送融云关闭麦位消息 TODO
    }

    @Override
    public void lock(Long userId, Integer roomId, Integer pwd) throws ServiceException {
        //角色类型1：房主 2：主持 3：管理
        List<Integer> roomUserRole = chatroomStaffService.getRoomUserRole(roomId, userId);
        if (!(roomUserRole.contains(1) || roomUserRole.contains(2) || roomUserRole.contains(3))) {
            throw new ServiceException(ResultCustomMessage.F1015);
        }
        String key = RedisKeyConstants.CACHE_CHATROOM_LOCK_KEY;
        intRedisTemplate.opsForZSet().add(key,roomId, pwd);

    }

    @Override
    public void unlock(Long userId, Integer roomId) throws ServiceException {
        //角色类型1：房主 2：主持 3：管理
        List<Integer> roomUserRole = chatroomStaffService.getRoomUserRole(roomId, userId);
        if (!(roomUserRole.contains(1) || roomUserRole.contains(2) || roomUserRole.contains(3))) {
            throw new ServiceException(ResultCustomMessage.F1015);
        }
        String key = RedisKeyConstants.CACHE_CHATROOM_LOCK_KEY;
        intRedisTemplate.opsForZSet().remove(key,roomId);
    }

    @Override
    public void addMicHeart(Integer roomId, List<Integer> positions, Integer heartValue) {
        String key =String.format(RedisKeyConstants.CACHE_CHATROOM_MIC_HEART_KEY,roomId);
        for(Integer position : positions){
            intRedisTemplate.opsForHash().increment(key,position,heartValue);
        }
        //发送麦位心动值变化消息 TODO
    }

    /**
     * 送礼增加用户财富魅力值
     * @param roomId
     * @param userId
     * @param targetUserIds
     * @param worth
     */
    @Override
    public void addUserRoomRank(Integer roomId, Long userId, List<Long> targetUserIds, Integer worth) {
        //查询月榜 周榜 日榜记录的key
        //月榜
        Date date = new Date();
        Integer week = DateUtil.getWeekByDate(date);
        String day = DateUtil.format(date,"yyyyMMdd");
        String month = DateUtil.format(date,"yyyyMM");
        String year = DateUtil.format(date,"yyyy");


        String sendMonthKey = String.format(RedisKeyConstants.CACHE_CHATROOM_RANK_SEND_MONTH_KEY,roomId,month);
        String sendWeekKey = String.format(RedisKeyConstants.CACHE_CHATROOM_RANK_SEND_WEEK_KEY,roomId,year,week);
        String sendDayKey = String.format(RedisKeyConstants.CACHE_CHATROOM_RANK_SEND_DAY_KEY,roomId,day);
        String sendKey = String.format(RedisKeyConstants.CACHE_RANK_SEND_MONTH_KEY,month);

        String receiveMonthKey = String.format(RedisKeyConstants.CACHE_CHATROOM_RANK_RECEIVE_MONTH_KEY,roomId,month);
        String receiveWeekKey = String.format(RedisKeyConstants.CACHE_CHATROOM_RANK_RECEIVE_WEEK_KEY,roomId,year,week);
        String receiveDayKey = String.format(RedisKeyConstants.CACHE_CHATROOM_RANK_RECEIVE_DAY_KEY,roomId,day);
        String receiveKey = String.format(RedisKeyConstants.CACHE_RANK_RECEIVE_MONTH_KEY,month);

        longRedisTemplate.opsForZSet().incrementScore(sendDayKey,userId,worth * targetUserIds.size());
        longRedisTemplate.expire(sendDayKey,3,TimeUnit.DAYS);
        longRedisTemplate.opsForZSet().incrementScore(sendWeekKey,userId,worth * targetUserIds.size());
        longRedisTemplate.expire(sendWeekKey,7,TimeUnit.DAYS);
        longRedisTemplate.opsForZSet().incrementScore(sendMonthKey,userId,worth * targetUserIds.size());
        longRedisTemplate.expire(sendMonthKey,30,TimeUnit.DAYS);
        longRedisTemplate.opsForZSet().incrementScore(sendKey,userId,worth * targetUserIds.size());
        longRedisTemplate.expire(sendMonthKey,30,TimeUnit.DAYS);

        for (Long targetUserId : targetUserIds) {
            longRedisTemplate.opsForZSet().incrementScore(receiveDayKey,targetUserId,worth);
            longRedisTemplate.expire(receiveDayKey,3,TimeUnit.DAYS);
            longRedisTemplate.opsForZSet().incrementScore(receiveWeekKey,targetUserId,worth);
            longRedisTemplate.expire(receiveWeekKey,7,TimeUnit.DAYS);
            longRedisTemplate.opsForZSet().incrementScore(receiveMonthKey,targetUserId,worth);
            longRedisTemplate.expire(receiveMonthKey,30,TimeUnit.DAYS);
            longRedisTemplate.opsForZSet().incrementScore(receiveKey,targetUserId,worth);
            longRedisTemplate.expire(receiveKey,30,TimeUnit.DAYS);
        }
    }


}