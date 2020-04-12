package com.play.product.service.impl;

import com.play.base.dao.IBaseDao;
import com.play.base.service.impl.BaseServiceImpl;
import com.play.product.dao.IGiftDao;
import com.play.product.model.Gift;
import com.play.product.service.IGiftService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by hushengmeng on 2020/3/30.
 */
@Service(value = "giftService")
public class GiftServiceImpl extends BaseServiceImpl<Gift, Long> implements IGiftService {
    @Resource
    private IGiftDao giftDao;
    @Resource(name = "redisTemplate")
    private RedisTemplate<String, Long> userRelRedisTemplate;

    @Override
    public IBaseDao<Gift> getBaseDao() {
        return giftDao;
    }


}
