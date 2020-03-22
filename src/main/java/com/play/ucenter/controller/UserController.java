package com.play.ucenter.controller;

import com.play.base.controller.BaseController;
import com.play.ucenter.service.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author hushengmeng
 * @date 2017/7/4.
 */
@Controller
@Scope("prototype")
@RequestMapping("user")
public class UserController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Resource
    IUserService userService;

    @Resource(name = "redisTemplate")
    private RedisTemplate redisTemplate;

    /**
     * 手机号登录
     * @param response
     * @param request
     */
    @RequestMapping("loginByMobile")
    public void loginByMobile(HttpServletResponse response, HttpServletRequest request) {
        send(resultResponse.success());
    }

}
