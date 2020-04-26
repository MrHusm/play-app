package com.play.im.view;

public class ChatroomStaffVO {

    private Integer roomId;

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
     * 头像地址
     */
    private String headUrl;

    /**
     * 性别 0：未知 1：男 2：女
     */
    private Integer sex;

    /**
     * vip等级
     */
    private Integer vipLevel;

    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
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

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Integer getVipLevel() {
        return vipLevel;
    }

    public void setVipLevel(Integer vipLevel) {
        this.vipLevel = vipLevel;
    }
}