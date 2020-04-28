package com.play.pay.service.impl;

import com.alibaba.fastjson.JSON;
import com.play.base.contants.RedisKeyConstants;
import com.play.base.dao.IBaseDao;
import com.play.base.service.impl.BaseServiceImpl;
import com.play.base.utils.BeanUtils;
import com.play.pay.dao.IRechargeItemDao;
import com.play.pay.model.RechargeItem;
import com.play.pay.service.IRechargeItemService;
import com.play.pay.view.RechargeItemVO;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Created by lenovo on 2017/8/6.
 */
@Service(value = "rechargeItemService")
public class RechargeItemServiceImpl extends BaseServiceImpl<RechargeItem, Integer> implements IRechargeItemService {
    @Resource(name = "rechargeItemDao")
    private IRechargeItemDao rechargeItemDao;

    @Resource(name = "redisTemplate")
    private RedisTemplate<String, String> stringRedisTemplate;

    @Override
    public IBaseDao<RechargeItem> getBaseDao() {
        return rechargeItemDao;
    }

    @Override
    public List<RechargeItemVO> getRechargeItem(Integer type) {
        String key = String.format(RedisKeyConstants.CACHE_RECHARGE_ITEM_LIST_KEY, type);
        List<String> rechargeItemJsons = stringRedisTemplate.opsForList().range(key, 0, -1);
        List<RechargeItemVO> rechargeItemVOS = rechargeItemJsons.stream().map(rechargeItemJson -> JSON.parseObject(rechargeItemJson, RechargeItemVO.class)).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(rechargeItemVOS)) {
            List<RechargeItem> rechargeItems = this.findListByParams("type", type);
            if (CollectionUtils.isNotEmpty(rechargeItems)) {
                rechargeItems.forEach(rechargeItem -> stringRedisTemplate.opsForList().leftPush(key, JSON.toJSONString(BeanUtils.copyProperties(RechargeItemVO.class, rechargeItem))));
                stringRedisTemplate.expire(key, 1, TimeUnit.DAYS);
            }
        }
        return rechargeItemVOS;
    }
}
