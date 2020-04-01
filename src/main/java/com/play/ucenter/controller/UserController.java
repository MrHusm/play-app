package com.play.ucenter.controller;

import com.play.base.controller.BaseController;
import com.play.base.exception.ServiceException;
import com.play.base.utils.DateUtil;
import com.play.base.utils.PageFinder;
import com.play.base.utils.Query;
import com.play.base.utils.ResultResponse;
import com.play.ucenter.model.User;
import com.play.ucenter.service.IUserAccountService;
import com.play.ucenter.service.IUserService;
import com.play.ucenter.view.UserVO;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author hushengmeng
 * @date 2017/7/4.
 */
@Controller
@Scope("prototype")
@RequestMapping("/user")
@Validated
public class UserController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Resource
    IUserService userService;

    @Resource
    IUserAccountService userAccountService;

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
    @ResponseBody
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
    @ResponseBody
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
     * 修改个人信息
     * @return
     * @throws ServiceException
     */
    @ResponseBody
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ResultResponse update(@RequestParam(required = false) String nickName, @RequestParam(required = false) String headUrl,
                                         @RequestParam(required = false) Integer sex, @RequestParam(required = false) String birthday,
                                         @RequestParam(required = false) String province, @RequestParam(required = false) String city,
                                         @RequestParam(required = false) String motto) {
        Long userId = this.getUserId();
        User user = new User();
        user.setUserId(userId);
        user.setNickName(nickName);
        if (StringUtils.isNotBlank(headUrl)) {
            user.setPendHeadUrl(headUrl);
        }
        user.setSex(sex);
        user.setBirthday(DateUtil.getDateByFormat(birthday,"yyyy-MM-dd"));
        user.setProvince(province);
        user.setCity(city);
        user.setMotto(motto);
        userService.updateUser(user);
        return resultResponse.success();
    }

    /**
     * 搜索用户
     *
     * @param keyword
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public ResultResponse search(@RequestParam(required = true) String keyword)throws ServiceException {
        List<UserVO> users = userService.search(keyword);
        return resultResponse.success(users);
    }

    /**
     * 用户详情
     */
    @ResponseBody
    @RequestMapping("/info")
    public ResultResponse info(@RequestParam(required = true) Long userId) {
        Long mId = this.getUserId();
        Map<String, Object> data = new HashMap<String, Object>();
        UserVO user = this.userService.getByUserId(mId, userId);
        //获取用户之间关系
        Integer relType = this.userService.getRelType(mId, userId);
        //获取粉丝数量
        Integer fansNum = this.userService.getRelationNum(2, userId);
        userService.addVisit(mId, userId);
        data.put("user", user);
        data.put("relType", relType);
        data.put("fansNum", fansNum);
        //TODO 获取魅力值 贡献值 礼物墙信息
        return resultResponse.success(data);
    }

    /**
     * 添加关注
     */
    @ResponseBody
    @RequestMapping("/addFollow")
    public ResultResponse addFollow(@RequestParam(required = true) Long userId) {
        Long mId = this.getUserId();
        this.userService.addFollow(mId, userId);
        return resultResponse.success();
    }

    /**
     * 添加关注
     */
    @ResponseBody
    @RequestMapping("/unFollow")
    public ResultResponse unFollow(@RequestParam(required = true) Long userId) {
        Long mId = this.getUserId();
        this.userService.unFollow(mId, userId);
        return resultResponse.success();
    }

    /**
     * 获取关系列表
     *
     * @param page
     * @param limit
     * @param type  1:关注 2：粉丝 3：好友 4：访客
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/relation/pager", method = {RequestMethod.GET})
    public ResultResponse followPager(@RequestParam(required = true) Integer type, @RequestParam(required = true) Integer page, @RequestParam(required = true) Integer limit) {
        Long userId = this.getUserId();
        Query query = new Query();
        query.setPage(page);
        query.setPage(limit);
        PageFinder pageFinder = userService.getRelationListByPager(type, userId, query);
        return resultResponse.success(pageFinder);
    }

    /**
     * 我的
     */
    @ResponseBody
    @RequestMapping("/mine")
    public ResultResponse mine(){
        Long userId = this.getUserId();
        Map<String, Object> data = new HashMap<String, Object>();
        UserVO user = this.userService.getByUserId(userId, userId);
        Integer followNum = userService.getRelationNum(1, userId);
        Integer fansNum = userService.getRelationNum(2, userId);
        Integer friendNum = userService.getRelationNum(3, userId);
        Integer visitNum = userService.getRelationNum(4, userId);
        data.put("user", user);
        data.put("followNum", followNum);
        data.put("fansNum", fansNum);
        data.put("friendNum", friendNum);
        data.put("visitNum", visitNum);
        return resultResponse.success(user);
    }

}
