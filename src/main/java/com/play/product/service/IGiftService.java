package com.play.product.service;


import com.play.base.exception.ServiceException;
import com.play.base.service.IBaseService;
import com.play.product.model.Gift;
import com.play.product.view.GiftVO;

import java.util.List;

/**
 * Created by hushengmeng on 2017/7/4.
 */
public interface IGiftService extends IBaseService<Gift, Integer> {

    GiftVO getById(Integer id);

    /**
     * 获取所有礼物信息
     *
     * @return
     */
    List<GiftVO> getAllGifts();
    /**
     * 打赏礼物
     *
     * @param giftId
     * @param giftNum       礼物数量
     * @param targetUserIds 接收人ID ','分隔
     * @param positions     麦位 ','分隔
     * @param roomId        聊天室id
     * @param payType       支付方式 0：金币 1：背包
     * @return
     * @throws ServiceException
     */
    void sendGift(Long userId, Integer roomId, Integer giftId, Integer giftNum, List<Long> targetUserIds, List<Integer> positions, Integer payType) throws ServiceException;

}
