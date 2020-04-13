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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

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

    /**打赏礼物
     *
     * @param giftId
     * @param giftNum 礼物数量
     * @param targetUserIds 接收人ID ','分隔
     * @param positions 麦位 ','分隔
     * @param roomId 聊天室id
     * @param payType 支付方式 0：金币 1：背包
     * @return
     * @throws ServiceException
     */
    @ResponseBody
    @RequestMapping(value = "/send", method = {RequestMethod.POST})
    public ResultResponse send(@RequestParam(required = true) Integer giftId, @RequestParam(required = true) Integer giftNum,
                               @RequestParam(required = true) String targetUserIds, @RequestParam(required = true) String positions,
                               @RequestParam(required = true) Integer roomId, @RequestParam(required = false) Integer payType) throws ServiceException {
        Long userId = this.getUserId();
        List<Long> targetUserIdList = new ArrayList<Long>();
        for (String targetUserId : targetUserIds.split(",")) {
            targetUserIdList.add(Long.parseLong(targetUserId));
        }
        List<Integer> positionList = new ArrayList<Integer>();
        for (String position : positions.split(",")) {
            positionList.add(Integer.parseInt(position));
        }
        if (positions != null) {
            positions.replace("0", "");
        }
        if (payType == null) {
            payType = 0;
        }
        if (giftNum <= 0) {
            giftNum = 1;
        }
        giftService.sendGift(userId, roomId, giftId, giftNum, targetUserIdList, positionList, payType);
        return resultResponse.success();

    }
}
