package com.play.message.view;

import com.play.product.view.GiftVO;

import java.util.Date;
import java.util.List;

/**
 * Created by lenovo on 2020/4/16.
 */
public class GiftMessageVO {

    private String orderNo;

    private Integer payType;

    private Long senderId;

    private List<Long> targetUserIds;

    private Integer roomId;

    private GiftVO gift;

    private Integer giftNum;

    private Date sendDate;

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public List<Long> getTargetUserIds() {
        return targetUserIds;
    }

    public void setTargetUserIds(List<Long> targetUserIds) {
        this.targetUserIds = targetUserIds;
    }

    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }

    public GiftVO getGift() {
        return gift;
    }

    public void setGift(GiftVO gift) {
        this.gift = gift;
    }

    public Integer getGiftNum() {
        return giftNum;
    }

    public void setGiftNum(Integer giftNum) {
        this.giftNum = giftNum;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public Date getSendDate() {
        return sendDate;
    }

    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }
}
