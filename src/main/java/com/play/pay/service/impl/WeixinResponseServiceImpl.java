package com.play.pay.service.impl;

import com.play.base.dao.IBaseDao;
import com.play.base.service.impl.BaseServiceImpl;
import com.play.pay.dao.IWeixinResponseDao;
import com.play.pay.model.WeixinResponse;
import com.play.pay.service.IWeixinResponseService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by lenovo on 2017/8/6.
 */
@Service(value = "weixinResponseService")
public class WeixinResponseServiceImpl extends BaseServiceImpl<WeixinResponse, Integer> implements IWeixinResponseService {
    @Resource(name = "weixinResponseDao")
    private IWeixinResponseDao weixinResponseDao;

    @Override
    public IBaseDao<WeixinResponse> getBaseDao() {
        return weixinResponseDao;
    }
}
