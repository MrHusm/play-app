package com.play.pay.service;

import com.play.base.exception.ServiceException;
import com.play.base.service.IBaseService;
import com.play.pay.model.AlipayOrder;

/**
 * Created by lenovo on 2017/8/6.
 */
public interface IAlipayOrderService extends IBaseService<AlipayOrder, Integer> {

    String order(Long userId, Integer productId) throws ServiceException;
}
