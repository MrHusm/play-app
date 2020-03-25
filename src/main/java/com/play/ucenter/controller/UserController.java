package com.play.ucenter.controller;

import com.play.base.controller.BaseController;
import com.play.base.exception.ServiceException;
import com.play.base.utils.ResultResponse;
import com.play.ucenter.model.User;
import com.play.ucenter.service.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author hushengmeng
 * @date 2017/7/4.
 */
@Controller
@Scope("prototype")
@RequestMapping("user")
@Validated
public class UserController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Resource
    IUserService userService;

//    @Resource(name = "redisTemplate")
//    private RedisTemplate redisTemplate;

    /**
     * 用户退出账号
     *
     * @param token
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/logout", method = {RequestMethod.POST})
    public ResultResponse logout(@RequestHeader(value = TOKEN, required = true) String token) throws ServiceException {
        userService.logout(token);
        return resultResponse.success();
    }

    /**
     * 发送手机号验证码
     *
     * @param mobile
     * @return
     */
    @RequestMapping(value = "/mobile/code")
    public ResultResponse code(@RequestParam(required = true) String mobile) {
        userService.sendMobileCode(mobile);
        return resultResponse.success();
    }

    /**
     * 手机号登录
     */
    @ResponseBody
    @RequestMapping("/loginByMobile")
    public ResultResponse loginByMobile(String token) {
        User user = new User();
        user.setName("我的");
        user.setCreateDate(new Date());
        return resultResponse.success(user);
    }


    /**
     * 用户详情
     */
    @RequestMapping("/info")
    public String info(@RequestParam(required = true) String token, Model model) throws ServiceException {
        User user = new User();
        user.setName("我的");
        user.setCreateDate(new Date());
        model.addAttribute("user", user);
        return "ucenter/info";
    }

}
