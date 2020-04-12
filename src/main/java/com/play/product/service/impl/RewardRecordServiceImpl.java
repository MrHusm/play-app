package com.play.product.service.impl;

import com.play.base.dao.IBaseDao;
import com.play.base.service.impl.BaseServiceImpl;
import com.play.product.dao.IRewardRecordDao;
import com.play.product.model.RewardRecord;
import com.play.product.service.IRewardRecordService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by hushengmeng on 2020/3/30.
 */
@Service(value = "rewardRecordService")
public class RewardRecordServiceImpl extends BaseServiceImpl<RewardRecord, Long> implements IRewardRecordService {
    @Resource
    private IRewardRecordDao rewardRecordDao;
    @Resource(name = "redisTemplate")
    private RedisTemplate<String, Long> userRelRedisTemplate;

    @Override
    public IBaseDao<RewardRecord> getBaseDao() {
        return rewardRecordDao;
    }


}
