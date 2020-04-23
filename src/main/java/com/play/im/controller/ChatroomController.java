package com.play.im.controller;

import com.play.base.controller.BaseController;
import com.play.base.exception.ServiceException;
import com.play.base.utils.PageFinder;
import com.play.base.utils.Query;
import com.play.base.utils.ResultResponse;
import com.play.im.model.Chatroom;
import com.play.im.service.IChatroomService;
import com.play.im.service.IChatroomStaffService;
import com.play.im.view.ChatroomStaffVO;
import com.play.im.view.ChatroomVO;
import com.play.ucenter.service.IUserService;
import com.play.ucenter.view.UserMicVO;
import com.play.ucenter.view.UserVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lenovo on 2020/4/1.
 */
@Controller
@Scope("prototype")
@RequestMapping("/chatroom")
@Validated
public class ChatroomController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(ChatroomController.class);
    @Resource
    IUserService userService;
    @Resource
    IChatroomService chatroomService;
    @Resource
    IChatroomStaffService chatroomStaffService;
    /**
     * 创建个人聊天室
     * @param name 聊天室名称
     * @param tagType 聊天室标签类型 1：女神 2：男神 3：电台
     * @return
     * @throws ServiceException
     */
    @ResponseBody
    @RequestMapping(value = "/create", method = {RequestMethod.POST})
    public ResultResponse create(@RequestParam(required = true) String name, @RequestParam(required = true) Integer tagType)
            throws ServiceException {
        Long userId = this.getUserId();
        Integer roomId = chatroomService.createChatroom(userId, name, tagType);
        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("roomId", roomId);
        return resultResponse.success(map);
    }

    /**
     * 获取个人聊天室列表
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/mine/list", method = {RequestMethod.GET})
    public ResultResponse mineList(){
        Long userId = this.getUserId();
        List<ChatroomVO> chatroomVOs = chatroomService.getMineList(userId);
        return resultResponse.success(chatroomVOs);
    }

    /**
     * 获取聊天室列表
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/list", method = {RequestMethod.GET})
    public ResultResponse list(@RequestParam(required = false) Integer tagType, @RequestParam(required = true)Integer page){
        Long userId = this.getUserId();
        Query query = new Query();
        query.setPage(page);
        query.setPageSize(20);
        Map<String,Object> condition = new HashMap<String, Object>();
        condition.put("tagType",tagType);
        PageFinder<Chatroom> pageFinder = this.chatroomService.findPageFinderObjs(condition,query);
        Map<String, Object> result = new HashMap<>();
        result.put("chatrooms", pageFinder.getData());
        result.put("hasPrevious", pageFinder.isHasPrevious());
        result.put("hasNext", pageFinder.isHasNext());
        result.put("pageNo", pageFinder.getPageNo());
        result.put("rowCount", pageFinder.getRowCount());
        return resultResponse.success(result);
    }
    /**
     * 加入聊天室
     *
     * @param roomId
     * @param pwd
     * @return
     * @throws ServiceException
     */
    @ResponseBody
    @RequestMapping(value = "/join", method = {RequestMethod.POST})
    public ResultResponse join(@RequestParam(required = true) Integer roomId, @RequestParam(required = false) Integer pwd) throws ServiceException {
        Long userId = this.getUserId();
        chatroomService.join(userId, roomId, pwd);
        return resultResponse.success();
    }

    /**
     * 离开聊天室
     *
     * @param roomId
     * @return
     * @throws ServiceException
     */
    @ResponseBody
    @RequestMapping(value = "/leave", method = {RequestMethod.POST})
    public ResultResponse leave(@RequestParam(required = true) Integer roomId) throws ServiceException {
        Long userId = this.getUserId();
        chatroomService.leave(roomId, userId);
        return resultResponse.success();
    }

    /**
     * 上麦
     */
    @ResponseBody
    @RequestMapping(value = "/mic/up", method = {RequestMethod.POST})
    public ResultResponse upMic(@RequestParam(required = true) Long micUserId, @RequestParam(required = true) Integer roomId, @RequestParam(required = true) Integer position) throws ServiceException {
        Long userId = this.getUserId();
        chatroomService.upMic(userId, micUserId, roomId, position);
        return resultResponse.success();
    }

    /**
     * 下麦
     */
    @ResponseBody
    @RequestMapping(value = "/mic/down", method = {RequestMethod.POST})
    public ResultResponse downMic(@RequestParam(required = true) Long micUserId, @RequestParam(required = true) Integer roomId) throws ServiceException {
        Long userId = this.getUserId();
        chatroomService.downMic(micUserId, roomId);
        return resultResponse.success();
    }

    /**
     * 取消排麦申请
     */
    @ResponseBody
    @RequestMapping(value = "/mic/up/cancel", method = {RequestMethod.POST})
    public ResultResponse cancelUpMic(@RequestParam(required = true) Long micUserId, @RequestParam(required = true) Integer roomId) throws ServiceException {
        Long userId = this.getUserId();
        chatroomService.cancelUpMic(micUserId, roomId);
        return resultResponse.success();
    }

    /**
     * 排麦列表
     */
    @ResponseBody
    @RequestMapping(value = "/mic/queue", method = {RequestMethod.GET})
    public ResultResponse micQueue(@RequestParam(required = true) Integer roomId) throws ServiceException {
        Long userId = this.getUserId();
        List<UserMicVO> userMicVOS = chatroomService.roomMicQueue(roomId);
        return resultResponse.success(userMicVOS);
    }

    /**
     * 聊天室用户列表
     */
    @RequestMapping(value = "/user/list", method = {RequestMethod.GET})
    public ResultResponse userList(@RequestParam(required = true) Integer roomId) throws ServiceException {
        Long userId = this.getUserId();
        List<UserVO> users = chatroomService.userList(userId, roomId);
        return resultResponse.success(users);
    }

    /**
     * 用户禁言
     */
    @ResponseBody
    @RequestMapping(value = "/add/nospeak", method = {RequestMethod.GET})
    public ResultResponse addNospeak(@RequestParam(required = true) Integer roomId, @RequestParam(required = true) Long userId, @RequestParam(required = true) Integer time) throws ServiceException {
        Long uid = this.getUserId();
        chatroomService.addNospeak(uid, userId, roomId, time);
        return resultResponse.success();
    }

    /**
     * 解除用户禁言
     */
    @ResponseBody
    @RequestMapping(value = "/remove/nospeak", method = {RequestMethod.GET})
    public ResultResponse removeNospeak(@RequestParam(required = true) Integer roomId, @RequestParam(required = true) Long userId) throws ServiceException {
        Long uid = this.getUserId();
        chatroomService.removeNospeak(uid, userId, roomId);
        return resultResponse.success();
    }

    /**
     * 禁言用户列表
     */
    @ResponseBody
    @RequestMapping(value = "/nospeak/list", method = {RequestMethod.GET})
    public ResultResponse nospeakList(@RequestParam(required = true) Integer roomId) throws ServiceException {
        Long userId = this.getUserId();
        List<UserVO> users = chatroomService.nospeakList(userId, roomId);
        return resultResponse.success(users);
    }

    /**
     * 用户加入黑名单 踢出房间
     */
    @ResponseBody
    @RequestMapping(value = "/add/black", method = {RequestMethod.GET})
    public ResultResponse addBlack(@RequestParam(required = true) Integer roomId, @RequestParam(required = true) Long userId, @RequestParam(required = true) Integer time) throws ServiceException {
        Long uid = this.getUserId();
        chatroomService.addBlack(uid, userId, roomId, time);
        return resultResponse.success();
    }

    /**
     * 解除用户黑名单
     */
    @ResponseBody
    @RequestMapping(value = "/remove/black", method = {RequestMethod.GET})
    public ResultResponse removeBlack(@RequestParam(required = true) Integer roomId, @RequestParam(required = true) Long userId) throws ServiceException {
        Long uid = this.getUserId();
        chatroomService.removeBlack(uid, userId, roomId);
        return resultResponse.success();
    }

    /**
     * 黑名单用户列表
     */
    @ResponseBody
    @RequestMapping(value = "/black/list", method = {RequestMethod.GET})
    public ResultResponse blackList(@RequestParam(required = true) Integer roomId) throws ServiceException {
        Long userId = this.getUserId();
        List<UserVO> users = chatroomService.blackList(userId, roomId);
        return resultResponse.success(users);
    }

    /**
     * 关闭房间
     */
    @ResponseBody
    @RequestMapping(value = "/close", method = {RequestMethod.GET})
    public ResultResponse close(@RequestParam(required = true) Integer roomId) throws ServiceException {
        Long userId = this.getUserId();
        chatroomService.close(userId, roomId);
        return resultResponse.success();
    }

    /**
     * 开启房间
     */
    @RequestMapping(value = "/open", method = {RequestMethod.GET})
    public ResultResponse open(@RequestParam(required = true) Integer roomId) throws ServiceException {
        Long userId = this.getUserId();
        chatroomService.open(userId, roomId);
        return resultResponse.success();
    }

    /**
     * 编辑房间信息 开关心动值 上下麦方式
     */
    @ResponseBody
    @RequestMapping(value = "/edit", method = {RequestMethod.GET})
    public ResultResponse edit(@RequestParam(required = true) Integer roomId, Chatroom chatroom) throws ServiceException {
        Long userId = this.getUserId();
        chatroom.setRoomId(roomId);
        chatroomService.updateChatroom(chatroom);
        return resultResponse.success();
    }

    /**
     * 添加聊天室工作人员
     * @param userId
     * @param type 用户类型 2：主持 3：管理
     * @return
     * @throws ServiceException
     */
    @RequestMapping(value = "/add/staff", method = {RequestMethod.GET})
    public ResultResponse addStaff(@RequestParam(required = true)Long userId,@RequestParam(required = true)Integer type,@RequestParam(required = true)Integer roomId) throws ServiceException {
        Long uid = this.getUserId();
        chatroomStaffService.addStaff(uid,roomId,userId,type);
        return resultResponse.success();
    }

    /**
     * 聊天室工作人员删除
     * @param userId
     * @param roomId
     * @return
     * @throws ServiceException
     */
    @ResponseBody
    @RequestMapping(value = "/delete/staff", method = {RequestMethod.GET})
    public ResultResponse deleteStaff(@RequestParam(required = true)Long userId, @RequestParam(required = true)Integer roomId) throws ServiceException {
        Long uid = this.getUserId();
        chatroomStaffService.deleteStaff(uid,roomId,userId);
        return resultResponse.success();
    }

    /**
     * 查询聊天室工作人员列表
     * @param roomId
     * @param type 用户类型 1：房主 2：主持 3：管理
     * @return
     * @throws ServiceException
     */
    @ResponseBody
    @RequestMapping(value = "/staff/list", method = {RequestMethod.GET})
    public ResultResponse staffList(@RequestParam(required = true)Long roomId, @RequestParam(required = true)Integer type) throws ServiceException {
        Long uid = this.getUserId();
        List<ChatroomStaffVO> list = chatroomStaffService.list(roomId,type);
        return resultResponse.success(list);
    }

    /**
     * 麦位倒计时
     * @param roomId
     * @param position 麦位
     * @param num 倒计时多少秒
     * @return
     * @throws ServiceException
     */
    @RequestMapping(value = "/timer/start", method = {RequestMethod.POST})
    public ResultResponse startTimer(@RequestParam(required = true) Integer roomId,@RequestParam(required = true) Integer position,@RequestParam(required = true) Integer num) throws ServiceException {
        Long userId = this.getUserId();
        chatroomService.startTimer(userId, roomId, position, num);
        return resultResponse.success();
    }

    /**
     * 麦位倒计时停止
     * @param roomId
     * @param position 麦位
     * @return
     * @throws ServiceException
     */
    @ResponseBody
    @RequestMapping(value = "/timer/stop", method = {RequestMethod.POST})
    public ResultResponse stopTimer(@RequestParam(required = true) Integer roomId, @RequestParam(required = true) Integer position) throws ServiceException {
        Long userId = this.getUserId();
        chatroomService.stopTimer(userId, roomId, position);
        return resultResponse.success();
    }

    /**
     * 用户收藏聊天室
     * @param roomId
     * @return
     * @throws ServiceException
     */
    @ResponseBody
    @RequestMapping(value = "/collect/add", method = {RequestMethod.POST})
    public ResultResponse addCollection(@RequestParam(required = true) Integer roomId) throws ServiceException {
        Long userId = this.getUserId();
        userService.addCollection(userId, roomId);
        return resultResponse.success();
    }

    /**
     * 用户取消收藏聊天室
     * @param roomId
     * @return
     * @throws ServiceException
     */
    @RequestMapping(value = "/collect/remove", method = {RequestMethod.POST})
    public ResultResponse removeCollection(@RequestParam(required = true) Integer roomId) throws ServiceException {
        Long userId = this.getUserId();
        userService.removeCollection(userId, roomId);
        return resultResponse.success();
    }

    /**
     * 聊天室加锁
     * @param roomId
     * @param pwd
     * @return
     * @throws ServiceException
     */
    @ResponseBody
    @RequestMapping(value = "/lock", method = {RequestMethod.POST})
    public ResultResponse lock(@RequestParam(required = true) Integer roomId, @RequestParam(required = true) Integer pwd) throws ServiceException {
        Long userId = this.getUserId();
        chatroomService.lock(userId, roomId,pwd);
        return resultResponse.success();
    }

    /**
     * 聊天室解锁
     * @param roomId
     * @param pwd
     * @return
     * @throws ServiceException
     */
    @ResponseBody
    @RequestMapping(value = "/unlock", method = {RequestMethod.POST})
    public ResultResponse unlock(@RequestParam(required = true) Integer roomId) throws ServiceException {
        Long userId = this.getUserId();
        chatroomService.unlock(userId, roomId);
        return resultResponse.success();
    }
}