package com.play.im.controller;

import com.play.base.controller.BaseController;
import com.play.base.exception.ServiceException;
import com.play.base.utils.PageFinder;
import com.play.base.utils.Query;
import com.play.base.utils.ResultResponse;
import com.play.im.model.Chatroom;
import com.play.im.service.IChatroomService;
import com.play.im.view.ChatroomVO;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestHeader;
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
     * 获取聊天室列表
     * @return
     */
    @RequestMapping(value = "/list", method = {RequestMethod.GET})
    public ResultResponse list(@RequestParam(required = false) Integer tagType, @RequestParam(required = true)Integer page){
        Long userId = this.getUserId();
        Query query = new Query();
        query.setPage(page);
        query.setPageSize(20);
        Map<String,Object> condition = new HashMap<String, Object>();
        condition.put("tagType",tagType);
        PageFinder<Chatroom> pageFinder = this.chatroomService.findPageFinderObjs(condition,query);
        return resultResponse.success(pageFinder);
    }
}