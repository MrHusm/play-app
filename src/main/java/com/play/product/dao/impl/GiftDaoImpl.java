package com.play.product.dao.impl;

import com.play.base.dao.IBaseDao;
import com.play.base.dao.impl.BaseDaoImpl;
import com.play.product.model.Gift;
import org.springframework.stereotype.Repository;

/**
 * Created by hushengmeng on 2017/7/4.
 */
@Repository(value = "giftDao")
public class GiftDaoImpl extends BaseDaoImpl<Gift> implements IBaseDao<Gift> {
}
