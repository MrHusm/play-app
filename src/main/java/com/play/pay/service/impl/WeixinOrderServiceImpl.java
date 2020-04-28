package com.play.pay.service.impl;

import com.play.base.dao.IBaseDao;
import com.play.base.service.impl.BaseServiceImpl;
import com.play.pay.dao.IWeixinOrderDao;
import com.play.pay.model.WeixinOrder;
import com.play.pay.service.IWeixinOrderService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by lenovo on 2017/8/6.
 */
@Service(value = "weixinOrderService")
public class WeixinOrderServiceImpl extends BaseServiceImpl<WeixinOrder, Integer> implements IWeixinOrderService {
    @Resource(name = "weixinOrderDao")
    private IWeixinOrderDao weixinOrderDao;

    @Override
    public IBaseDao<WeixinOrder> getBaseDao() {
        return weixinOrderDao;
    }
}
