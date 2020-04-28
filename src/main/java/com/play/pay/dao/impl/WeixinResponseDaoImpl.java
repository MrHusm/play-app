package com.play.pay.dao.impl;

import com.play.base.dao.impl.BaseDaoImpl;
import com.play.pay.dao.IWeixinResponseDao;
import com.play.pay.model.WeixinResponse;
import org.springframework.stereotype.Repository;

/**
 * Created by lenovo on 2017/8/6.
 */
@Repository(value = "weixinResponseDao")
public class WeixinResponseDaoImpl extends BaseDaoImpl<WeixinResponse> implements IWeixinResponseDao {
}
