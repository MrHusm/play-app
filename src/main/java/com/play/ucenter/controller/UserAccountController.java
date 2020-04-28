package com.play.ucenter.controller;

import com.play.base.controller.BaseController;
import com.play.base.exception.ServiceException;
import com.play.base.utils.BeanUtils;
import com.play.base.utils.PageFinder;
import com.play.base.utils.Query;
import com.play.base.utils.ResultResponse;
import com.play.ucenter.model.TradeRecord;
import com.play.ucenter.service.ITradeRecordService;
import com.play.ucenter.service.IUserAccountService;
import com.play.ucenter.service.IUserService;
import com.play.ucenter.view.TradeRecordVO;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    @Resource
    ITradeRecordService tradeRecordService;

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

    /**
     * 账户相关记录
     *
     * @param type 0：收入 1：支出
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/trade/record")
    public ResultResponse tradeRecord(@RequestParam(required = true) Integer type, @RequestParam(required = true) Integer page) throws ServiceException {
        Long userId = this.getUserId();
        Query query = new Query();
        query.setPage(page);
        query.setPageSize(20);
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put("userId", userId);
        condition.put("status", type);
        PageFinder<TradeRecord> pageFinder = this.tradeRecordService.findPageFinderObjs(condition, query);
        List<TradeRecordVO> data = pageFinder.getData().stream().map(tradeRecord -> BeanUtils.copyProperties(TradeRecordVO.class, tradeRecord)).collect(Collectors.toList());
        Map<String, Object> result = new HashMap<>();
        result.put("tradeRecords", data);
        result.put("hasPrevious", pageFinder.isHasPrevious());
        result.put("hasNext", pageFinder.isHasNext());
        result.put("pageNo", pageFinder.getPageNo());
        result.put("rowCount", pageFinder.getRowCount());
        return resultResponse.success(result);
    }

}
