package com.play.product.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class RewardRecord implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 订单号
     */
    private Long orderId;

    /**
     * 打赏人id
     */
    private Long senderId;

    /**
     * 接收人id
     */
    private Long receiverId;

    /**
     * 聊天室拥有人id
     */
    private Long roomOwnerId;

    /**
     * 礼物id
     */
    private Integer giftId;

    /**
     * 礼物名称
     */
    private String giftName;

    /**
     * 礼物数量
     */
    private Integer giftNum;

    /**
     * 礼物单价
     */
    private BigDecimal giftPrice;

    /**
     * 礼物总价
     */
    private BigDecimal giftTotalPrice;

    /**
     * 接收人收益
     */
    private BigDecimal receiverIncome;

    /**
     * 聊天室拥有人收益
     */
    private BigDecimal roomOwnerIncome;

    /**
     * 平台收益
     */
    private BigDecimal platformIncome;

    /**
     * 支付方式 0：金币 1：背包
     */
    private Boolean payType;

    /**
     * 创建人
     */
    private Long creator;

    /**
     * 创建时间
     */
    private Date createDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }

    public Long getRoomOwnerId() {
        return roomOwnerId;
    }

    public void setRoomOwnerId(Long roomOwnerId) {
        this.roomOwnerId = roomOwnerId;
    }

    public Integer getGiftId() {
        return giftId;
    }

    public void setGiftId(Integer giftId) {
        this.giftId = giftId;
    }

    public String getGiftName() {
        return giftName;
    }

    public void setGiftName(String giftName) {
        this.giftName = giftName;
    }

    public Integer getGiftNum() {
        return giftNum;
    }

    public void setGiftNum(Integer giftNum) {
        this.giftNum = giftNum;
    }

    public BigDecimal getGiftPrice() {
        return giftPrice;
    }

    public void setGiftPrice(BigDecimal giftPrice) {
        this.giftPrice = giftPrice;
    }

    public BigDecimal getGiftTotalPrice() {
        return giftTotalPrice;
    }

    public void setGiftTotalPrice(BigDecimal giftTotalPrice) {
        this.giftTotalPrice = giftTotalPrice;
    }

    public BigDecimal getReceiverIncome() {
        return receiverIncome;
    }

    public void setReceiverIncome(BigDecimal receiverIncome) {
        this.receiverIncome = receiverIncome;
    }

    public BigDecimal getRoomOwnerIncome() {
        return roomOwnerIncome;
    }

    public void setRoomOwnerIncome(BigDecimal roomOwnerIncome) {
        this.roomOwnerIncome = roomOwnerIncome;
    }

    public BigDecimal getPlatformIncome() {
        return platformIncome;
    }

    public void setPlatformIncome(BigDecimal platformIncome) {
        this.platformIncome = platformIncome;
    }

    public Boolean getPayType() {
        return payType;
    }

    public void setPayType(Boolean payType) {
        this.payType = payType;
    }

    public Long getCreator() {
        return creator;
    }

    public void setCreator(Long creator) {
        this.creator = creator;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}