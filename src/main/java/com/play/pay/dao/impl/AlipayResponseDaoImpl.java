package com.play.pay.dao.impl;

import com.play.base.dao.impl.BaseDaoImpl;
import com.play.pay.dao.IAlipayResponseDao;
import com.play.pay.model.AlipayResponse;
import org.springframework.stereotype.Repository;

/**
 * Created by lenovo on 2017/8/6.
 */
@Repository(value = "alipayResponseDao")
public class AlipayResponseDaoImpl extends BaseDaoImpl<AlipayResponse> implements IAlipayResponseDao {
}
