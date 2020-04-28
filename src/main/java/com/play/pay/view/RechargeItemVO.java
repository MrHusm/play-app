package com.play.pay.view;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
 * @author hushengmeng
 * @date 2017/8/14.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RechargeItemVO implements Serializable {

    private Integer id;

    /**
     * 价格（元）
     */
    private Double price;

    /**
     * 充值金额(钻)
     */
    private Integer money;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getMoney() {
        return money;
    }

    public void setMoney(Integer money) {
        this.money = money;
    }
}
