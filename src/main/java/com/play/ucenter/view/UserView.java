package com.play.ucenter.view;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by lenovo on 2020/3/26.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserView implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long userId;

    private String token;

    /**
     * 融云token
     */
    private String rongToken;

    /**
     * 靓号
     */
    private Long prettyId;

    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 银币数量
     */
    private BigDecimal silverAmount;

    /**
     * 金币数量
     */
    private BigDecimal goldAmount;

    /**
     * 头像地址
     */
    private String headUrl;

    /**
     * 待审头像地址
     */
    private String pendHeadUrl;

    /**
     * 头像框地址
     */
    private String headwearUrl;

    /**
     * 性别 0：未知 1：男 2：女
     */
    private Integer sex;

    /**
     * 生日
     */
    private Date birthday;

    /**
     * 省
     */
    private String province;

    /**
     * 市
     */
    private String city;

    /**
     * 签名
     */
    private String motto;

    /**
     * vip等级
     */
    private Byte vipLevel;

    /**
     * vip经验
     */
    private Integer vipExp;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getPrettyId() {
        return prettyId;
    }

    public void setPrettyId(Long prettyId) {
        this.prettyId = prettyId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public String getHeadwearUrl() {
        return headwearUrl;
    }

    public void setHeadwearUrl(String headwearUrl) {
        this.headwearUrl = headwearUrl;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getMotto() {
        return motto;
    }

    public void setMotto(String motto) {
        this.motto = motto;
    }

    public Byte getVipLevel() {
        return vipLevel;
    }

    public void setVipLevel(Byte vipLevel) {
        this.vipLevel = vipLevel;
    }

    public Integer getVipExp() {
        return vipExp;
    }

    public void setVipExp(Integer vipExp) {
        this.vipExp = vipExp;
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

    public String getPendHeadUrl() {
        return pendHeadUrl;
    }

    public void setPendHeadUrl(String pendHeadUrl) {
        this.pendHeadUrl = pendHeadUrl;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRongToken() {
        return rongToken;
    }

    public void setRongToken(String rongToken) {
        this.rongToken = rongToken;
    }
}