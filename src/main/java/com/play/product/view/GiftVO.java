package com.play.product.view;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.math.BigDecimal;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class GiftVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;

    /**
     * 礼物名称
     */
    private String name;

    /**
     * 图标地址
     */
    private String imageUrl;

    /**
     * 特效地址
     */
    private String svgaUrl;

    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * 价值
     */
    private BigDecimal worth;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 心动值
     */
    private Integer heartValue;

    /**
     * 是否上架 1：上架 0：下架
     */
    private Integer status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getSvgaUrl() {
        return svgaUrl;
    }

    public void setSvgaUrl(String svgaUrl) {
        this.svgaUrl = svgaUrl;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getWorth() {
        return worth;
    }

    public void setWorth(BigDecimal worth) {
        this.worth = worth;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Integer getHeartValue() {
        return heartValue;
    }

    public void setHeartValue(Integer heartValue) {
        this.heartValue = heartValue;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
