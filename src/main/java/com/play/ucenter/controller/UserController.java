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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
