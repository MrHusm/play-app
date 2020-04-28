package com.play.pay.dao.impl;

import com.play.base.dao.impl.BaseDaoImpl;
import com.play.pay.dao.IRechargeItemDao;
import com.play.pay.model.RechargeItem;
import org.springframework.stereotype.Repository;

/**
 * Created by lenovo on 2017/8/6.
 */
@Repository(value = "rechargeItemDao")
public class RechargeItemDaoImpl extends BaseDaoImpl<RechargeItem> implements IRechargeItemDao {

}
