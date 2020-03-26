package com.play.ucenter.model;

import java.math.BigDecimal;
import java.util.Date;

public class UserAccount {
    private Long id;

    private Long userId;

    /**
     * 银币数量
     */
    private BigDecimal silverAmount;

    /**
     * 金币数量
     */
    private BigDecimal goldAmount;

    private Date createDate;

    private Date updateDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public BigDecimal getSilverAmount() {
        return silverAmount;
    }

    public void setSilverAmount(BigDecimal silverAmount) {
        this.silverAmount = silverAmount;
    }

    public BigDecimal getGoldAmount() {
        return goldAmount;
    }

    public void setGoldAmount(BigDecimal goldAmount) {
        this.goldAmount = goldAmount;
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