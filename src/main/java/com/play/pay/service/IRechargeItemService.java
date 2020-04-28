package com.play.pay.service;

import com.play.base.service.IBaseService;
import com.play.pay.model.RechargeItem;
import com.play.pay.view.RechargeItemVO;

import java.util.List;

/**
 * Created by lenovo on 2017/8/6.
 */
public interface IRechargeItemService extends IBaseService<RechargeItem, Integer> {


    /**
     * 获取充值信息
     *
     * @param type 类型 1：通用 2：微信公众号
     * @return
     */
    List<RechargeItemVO> getRechargeItem(Integer type);
}
