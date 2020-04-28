package com.play.pay.service.impl;


import com.play.base.dao.IBaseDao;
import com.play.base.service.impl.BaseServiceImpl;
import com.play.pay.dao.IAlipayResponseDao;
import com.play.pay.model.AlipayResponse;
import com.play.pay.service.IAlipayResponseService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by lenovo on 2017/8/6.
 */
@Service(value = "alipayResponseService")
public class AlipayResponseServiceImpl extends BaseServiceImpl<AlipayResponse, Integer> implements IAlipayResponseService {
    @Resource(name = "alipayResponseDao")
    private IAlipayResponseDao alipayResponseDao;

    @Override
    public IBaseDao<AlipayResponse> getBaseDao() {
        return alipayResponseDao;
    }
}
