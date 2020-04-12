package com.play.ucenter.service.impl;

import com.play.base.dao.IBaseDao;
import com.play.base.service.impl.BaseServiceImpl;
import com.play.ucenter.dao.ITradeRecordDao;
import com.play.ucenter.model.TradeRecord;
import com.play.ucenter.service.ITradeRecordService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by hushengmeng on 2020/3/30.
 */
@Service(value = "tradeRecordService")
public class TradeRecordServiceImpl extends BaseServiceImpl<TradeRecord, Long> implements ITradeRecordService {

    @Resource
    private ITradeRecordDao tradeRecordDao;

    @Override
    public IBaseDao<TradeRecord> getBaseDao() {
        return tradeRecordDao;
    }
}
