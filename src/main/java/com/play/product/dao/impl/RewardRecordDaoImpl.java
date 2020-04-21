package com.play.product.dao.impl;

import com.play.base.dao.impl.BaseDaoImpl;
import com.play.product.dao.IRewardRecordDao;
import com.play.product.model.RewardRecord;
import org.springframework.stereotype.Repository;

/**
 * Created by hushengmeng on 2017/7/4.
 */
@Repository(value = "rewardRecordDao")
public class RewardRecordDaoImpl extends BaseDaoImpl<RewardRecord> implements IRewardRecordDao {
}
