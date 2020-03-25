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
import java.util.Map;

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
    public ResultResponse code(@RequestParam(required = true) String mobile)throws ServiceException {
        userService.sendMobileCode(mobile);
        return resultResponse.success();
    }

    /**
     * 手机号登录
     * @param mobile
     * @param code
     * @return
     * @throws ServiceException
     */
    @RequestMapping(value = "/mobile/login")
    public ResultResponse verifyByMobile(@RequestParam(required = true) String mobile, @RequestParam(required = true) String code,
                                         @RequestParam(required = true) String appVersion, @RequestParam(required = true) Integer deviceType,
                                         @RequestParam(required = true) String deviceImei, @RequestParam(required = true) String deviceName,
                                         @RequestParam(required = true) String bundleId, @RequestParam(required = true) String osVersion,
                                         @RequestParam(required = true) Integer channel)throws ServiceException {
        User loginUser = new User();
        loginUser.setMobile(mobile);
        loginUser.setAppVersion(appVersion);
        loginUser.setDeviceType(deviceType);
        loginUser.setDeviceImei(deviceImei);
        loginUser.setDeviceName(deviceName);
        loginUser.setBundleId(bundleId);
        loginUser.setOsVersion(osVersion);
        loginUser.setChannel(channel);
        Map<String,Object> data = userService.loginByMobile(mobile, code, loginUser);
        return resultResponse.success(data);
    }



    /**
     * 用户详情
     */
    @RequestMapping("/info")
    public String info(@RequestParam(required = true) String token, Model model) throws ServiceException {
        User user = new User();
        user.setCreateDate(new Date());
        model.addAttribute("user", user);
        return "ucenter/info";
    }

}
