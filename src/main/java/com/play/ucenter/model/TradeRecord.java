package com.play.ucenter.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TradeRecord implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    private String orderNo;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 交易类型 1：礼物收入 2:微信充值 3：支付宝充值 4：后台转入 5：用户转入 6：礼物分成 7:兑换 -1：打赏 -2：用户转出 -3：抽奖
     */
    private Integer type;

    /**
     * 账户类型 1：金币 2：银币
     */
    private Integer accountType;

    /**
     * 交易时间
     */
    private Date tradeTime;

    /**
     * 交易金额
     */
    private BigDecimal amount;

    private Long creator;

    private Date createDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getAccountType() {
        return accountType;
    }

    public void setAccountType(Integer accountType) {
        this.accountType = accountType;
    }

    public Date getTradeTime() {
        return tradeTime;
    }

    public void setTradeTime(Date tradeTime) {
        this.tradeTime = tradeTime;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
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