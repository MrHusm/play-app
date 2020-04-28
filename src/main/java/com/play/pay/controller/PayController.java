package com.play.pay.controller;

import com.play.base.controller.BaseController;
import com.play.base.utils.ResultResponse;
import com.play.pay.service.IRechargeItemService;
import com.play.pay.view.RechargeItemVO;
import com.play.ucenter.model.UserAccount;
import com.play.ucenter.service.IUserAccountService;
import com.play.ucenter.service.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Scope("prototype")
@RequestMapping("/pay")
@Validated
public class PayController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(PayController.class);

    @Resource(name = "rechargeItemService")
    IRechargeItemService rechargeItemService;

    @Resource(name = "userAccountService")
    IUserAccountService userAccountService;

    @Resource(name = "userService")
    IUserService userService;


    /**
     * 获取充值档位信息
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/items")
    public ResultResponse items() {
        Long userId = this.getUserId();
        UserAccount userAccount = userAccountService.getByUserId(userId);
        List<RechargeItemVO> rechargeItems = this.rechargeItemService.getRechargeItem(1);
        Map<String, Object> result = new HashMap<>();
        result.put("silverAmount", userAccount.getSilverAmount());
        result.put("goldAmount", userAccount.getGoldAmount());
        result.put("rechargeItems", rechargeItems);
        return resultResponse.success(result);
    }


}
