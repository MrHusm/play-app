package com.play.ucenter.controller;

import com.play.base.controller.BaseController;
import com.play.base.exception.ServiceException;
import com.play.base.utils.ResultResponse;
import com.play.ucenter.service.IUserAccountService;
import com.play.ucenter.service.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * @author hushengmeng
 * @date 2017/7/4.
 */
@Controller
@Scope("prototype")
@RequestMapping("/account")
@Validated
public class UserAccountController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(UserAccountController.class);

    @Resource
    IUserService userService;

    @Resource
    IUserAccountService userAccountService;

    /**
     * 转账
     *
     * @param targetUserId
     * @param amount       金额
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/transfer")
    public ResultResponse transferAccount(@RequestParam(required = true) Long targetUserId, @RequestParam(required = true) BigDecimal amount) throws ServiceException {
        Long userId = this.getUserId();
        userAccountService.transferAccount(userId, targetUserId, amount);
        return resultResponse.success();
    }

    /**
     * 兑换
     *
     * @param amount 金额
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/exchange")
    public ResultResponse exchange(@RequestParam(required = true) BigDecimal amount) throws ServiceException {
        Long userId = this.getUserId();
        userAccountService.exchange(userId, amount);
        return resultResponse.success();
    }

    /**
     * 管理员充值
     *
     * @param amount 金额
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/admin/recharge")
    public ResultResponse adminRecharge(@RequestParam(required = true) Long targetUserId, @RequestParam(required = true) BigDecimal amount) throws ServiceException {
        Long userId = this.getUserId();
        userAccountService.adminRecharge(userId, targetUserId, amount);
        return resultResponse.success();
    }

}
