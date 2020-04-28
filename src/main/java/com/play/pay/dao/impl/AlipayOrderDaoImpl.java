package com.play.pay.dao.impl;

import com.play.base.dao.impl.BaseDaoImpl;
import com.play.pay.dao.IAlipayOrderDao;
import com.play.pay.model.AlipayOrder;
import org.springframework.stereotype.Repository;

/**
 * Created by lenovo on 2017/8/6.
 */
@Repository(value = "alipayOrderDao")
public class AlipayOrderDaoImpl extends BaseDaoImpl<AlipayOrder> implements IAlipayOrderDao {
}
