package com.play.im.view;

public class RoomMicVO {
    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 靓号
     */
    private Long prettyId;

    /**
     * 用户名称
     */
    private String nickName;

    /**
     * 头像地址
     */
    private String headUrl;

    /**
     * 麦位心动值
     */
    private Integer heartValue;

    /**
     * 禁麦状态 0：未禁麦 1：禁麦
     */
    private Integer status;

    /**
     * 倒计时结束时间
     */
    private Integer endTime;

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

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
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

    public Integer getEndTime() {
        return endTime;
    }

    public void setEndTime(Integer endTime) {
        this.endTime = endTime;
    }
}