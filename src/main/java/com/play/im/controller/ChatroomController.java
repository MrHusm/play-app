package com.play.im.controller;

import com.play.base.controller.BaseController;
import com.play.base.exception.ServiceException;
import com.play.base.utils.ResultResponse;
import com.play.im.service.IChatroomService;
import com.play.im.view.ChatroomVO;
import com.play.ucenter.view.UserMicVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

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
    IChatroomService chatroomService;
    /**
     * 创建个人聊天室
     * @param name 聊天室名称
     * @param tagType 聊天室标签类型 1：女神 2：男神 3：电台
     * @return
     * @throws ServiceException
     */
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
    @RequestMapping(value = "/mine/list", method = {RequestMethod.GET})
    public ResultResponse mineList(){
        Long userId = this.getUserId();
        List<ChatroomVO> chatroomVOs = chatroomService.getMineList(userId);
        return resultResponse.success(chatroomVOs);
    }

    /**
     * 加入聊天室
     *
     * @param roomId
     * @param pwd
     * @return
     * @throws ServiceException
     */
    @RequestMapping(value = "/join", method = {RequestMethod.POST})
    public ResultResponse join(@RequestParam(required = true) Integer roomId, @RequestParam(required = false) Integer pwd) throws ServiceException {
        Long userId = this.getUserId();
        //获取房间基本信息
        chatroomService.join(userId, roomId, pwd);
        return resultResponse.success();
    }

    /**
     * 上麦
     */
    @RequestMapping(value = "/mic/up", method = {RequestMethod.POST})
    public ResultResponse upMic(@RequestParam(required = true) Long micUserId, @RequestParam(required = true) Integer roomId, @RequestParam(required = true) Integer position) throws ServiceException {
        Long userId = this.getUserId();
        chatroomService.upMic(userId, micUserId, roomId, position);
        return resultResponse.success();
    }

    /**
     * 下麦
     */
    @RequestMapping(value = "/mic/down", method = {RequestMethod.POST})
    public ResultResponse downMic(@RequestParam(required = true) Long micUserId, @RequestParam(required = true) Integer roomId) throws ServiceException {
        Long userId = this.getUserId();
        chatroomService.downMic(micUserId, roomId);
        return resultResponse.success();
    }

    /**
     * 取消排麦申请
     */
    @RequestMapping(value = "/mic/up/cancel", method = {RequestMethod.POST})
    public ResultResponse cancelUpMic(@RequestParam(required = true) Long micUserId, @RequestParam(required = true) Integer roomId) throws ServiceException {
        Long userId = this.getUserId();
        chatroomService.cancelUpMic(micUserId, roomId);
        return resultResponse.success();
    }

    /**
     * 排麦列表
     */
    @RequestMapping(value = "/mic/queue", method = {RequestMethod.GET})
    public ResultResponse micQueue(@RequestParam(required = true) Integer roomId) throws ServiceException {
        Long userId = this.getUserId();
        List<UserMicVO> userMicVOS = chatroomService.roomMicQueue(roomId);
        return resultResponse.success(userMicVOS);
    }

}