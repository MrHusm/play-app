package com.play.pay.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author hushengmeng
 * @date 2017/8/14.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RechargeItem implements Serializable {

    private static final long serialVersionUID = -1665208392379794212L;

    private Integer id;

    /**
     * 价格（元）
     */
    private BigDecimal price;

    /**
     * 充值金额(钻)
     */
    private Integer money;

    /**
     * 类型 1：通用 2：微信公众号
     */
    private Integer type;

    private Date createDate;

    private Date updateDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getMoney() {
        return money;
    }

    public void setMoney(Integer money) {
        this.money = money;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
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
}
