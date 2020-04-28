package com.play.pay.dao.impl;

import com.play.base.dao.impl.BaseDaoImpl;
import com.play.pay.dao.IWeixinOrderDao;
import com.play.pay.model.WeixinOrder;
import org.springframework.stereotype.Repository;

/**
 * Created by lenovo on 2017/8/6.
 */
@Repository(value = "weixinOrderDao")
public class WeixinOrderDaoImpl extends BaseDaoImpl<WeixinOrder> implements IWeixinOrderDao {
}
