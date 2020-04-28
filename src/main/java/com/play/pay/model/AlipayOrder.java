package com.play.pay.model;


import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by lenovo on 2017/8/6.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AlipayOrder implements Serializable {

    private static final long serialVersionUID = -7053180264632214460L;

    private Integer id;

    private Long userId;

    /**
     * 购买的产品ID
     */
    private Integer productId;

    /**
     * 订单号
     */
    private String WIDoutTradeNo;

    /**
     * 商品名称
     */
    private String WIDsubject;

    /**
     * 付款金额
     */
    private BigDecimal WIDtotalAmount;

    /**
     * 商品描述
     */
    private String WIDbody;

    /**
     * 类型 ：1：充值
     */
    private Integer type;

    /**
     * 备注
     */
    private String comment;

    private Date createDate;

    private Date updateDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getWIDoutTradeNo() {
        return WIDoutTradeNo;
    }

    public void setWIDoutTradeNo(String WIDoutTradeNo) {
        this.WIDoutTradeNo = WIDoutTradeNo;
    }

    public String getWIDsubject() {
        return WIDsubject;
    }

    public void setWIDsubject(String WIDsubject) {
        this.WIDsubject = WIDsubject;
    }

    public BigDecimal getWIDtotalAmount() {
        return WIDtotalAmount;
    }

    public void setWIDtotalAmount(BigDecimal WIDtotalAmount) {
        this.WIDtotalAmount = WIDtotalAmount;
    }

    public String getWIDbody() {
        return WIDbody;
    }

    public void setWIDbody(String WIDbody) {
        this.WIDbody = WIDbody;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
