package com.play.product.controller;

import com.play.base.controller.BaseController;
import com.play.base.exception.ServiceException;
import com.play.base.utils.ResultResponse;
import com.play.product.service.IGiftService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * @author hushengmeng
 * @date 2017/7/4.
 */
@Controller
@Scope("prototype")
@RequestMapping("/gift")
@Validated
public class GiftController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(GiftController.class);

    @Resource
    IGiftService giftService;


    /**
     * 用户退出账号
     *
     * @param token
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/logout", method = {RequestMethod.POST})
    public ResultResponse logout(@RequestHeader(value = TOKEN, required = true) String token) throws ServiceException {

        return resultResponse.success();
    }


}
