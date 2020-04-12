package com.play.ucenter.dao.impl;

import com.play.base.dao.impl.BaseDaoImpl;
import com.play.ucenter.dao.ITradeRecordDao;
import com.play.ucenter.model.TradeRecord;
import org.springframework.stereotype.Repository;

/**
 * Created by hushengmeng on 2017/7/4.
 */
@Repository(value="tradeRecordDao")
public class TradeRecordDaoImpl extends BaseDaoImpl<TradeRecord> implements ITradeRecordDao {
}
