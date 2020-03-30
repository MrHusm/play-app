package com.play.ucenter.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class User implements Serializable{
    private static final long serialVersionUID = 1L;

    private Long id;

    private Long userId;

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
     * 登录密码
     */
    private String pwd;

    /**
     * 头像地址
     */
    private String headUrl;

    /**
     * 待审核头像地址
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

    /**
     * 账号状态 0 正常 1 已冻结
     */
    private Boolean freeze;

    /**
     * 冻结结束时间
     */
    private Date freezeExpireTime;

    /**
     * APP版本
     */
    private String appVersion;

    /**
     * 设备类型 0：Android 1：IOS
     */
    private Integer deviceType;

    /**
     * 设备序列号
     */
    private String deviceImei;

    /**
     * 设备名称
     */
    private String deviceName;

    /**
     * bundleId
     */
    private String bundleId;

    /**
     * 系统版本
     */
    private String osVersion;

    /**
     * 渠道
     */
    private Integer channel;

    /**
     * 是否删除 0 删除 1 未删除
     */
    private Boolean deleted;

    /**
     * 注册时间
     */
    private Date createDate;

    /**
     * 修改时间
     */
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

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
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

    public Boolean getFreeze() {
        return freeze;
    }

    public void setFreeze(Boolean freeze) {
        this.freeze = freeze;
    }

    public Date getFreezeExpireTime() {
        return freezeExpireTime;
    }

    public void setFreezeExpireTime(Date freezeExpireTime) {
        this.freezeExpireTime = freezeExpireTime;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public Integer getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(Integer deviceType) {
        this.deviceType = deviceType;
    }

    public String getDeviceImei() {
        return deviceImei;
    }

    public void setDeviceImei(String deviceImei) {
        this.deviceImei = deviceImei;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getBundleId() {
        return bundleId;
    }

    public void setBundleId(String bundleId) {
        this.bundleId = bundleId;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public Integer getChannel() {
        return channel;
    }

    public void setChannel(Integer channel) {
        this.channel = channel;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
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

    public String getPendHeadUrl() {
        return pendHeadUrl;
    }

    public void setPendHeadUrl(String pendHeadUrl) {
        this.pendHeadUrl = pendHeadUrl;
    }
}
