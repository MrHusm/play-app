package com.play.im.model;

import java.util.Date;

public class Chatroom {
    private Integer id;

    /**
     * 聊天室ID
     */
    private Integer roomId;

    /**
     * 聊天室名称
     */
    private String name;

    /**
     * 聊天室类型 1：个人厅 2：公会厅
     */
    private Integer type;

    /**
     * 聊天室头像地址
     */
    private String imgUrl;

    /**
     * 聊天室背景图片url
     */
    private String bgImgUrl;

    /**
     * 公告
     */
    private String notice;

    /**
     * 欢迎语
     */
    private String slogan;

    /**
     * 1：开启 2：关闭 3：冻结
     */
    private Integer status;

    /**
     * 聊天室所属人id
     */
    private Long ownnerId;

    /**
     * 0:隐藏心动值 1 显示心动值
     */
    private Integer displayHeart;

    /**
     * 聊天室标签类型 1：女神 2：男神 3：电台
     */
    private Integer tagType;

    /**
     * 上麦方式1:审批上麦2：自由上麦
     */
    private Integer micType;

    /**
     * 公会id
     */
    private Integer guildId;

    private Date updateDate;

    private Date createDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getBgImgUrl() {
        return bgImgUrl;
    }

    public void setBgImgUrl(String bgImgUrl) {
        this.bgImgUrl = bgImgUrl;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public String getSlogan() {
        return slogan;
    }

    public void setSlogan(String slogan) {
        this.slogan = slogan;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getOwnnerId() {
        return ownnerId;
    }

    public void setOwnnerId(Long ownnerId) {
        this.ownnerId = ownnerId;
    }

    public Integer getDisplayHeart() {
        return displayHeart;
    }

    public void setDisplayHeart(Integer displayHeart) {
        this.displayHeart = displayHeart;
    }

    public Integer getTagType() {
        return tagType;
    }

    public void setTagType(Integer tagType) {
        this.tagType = tagType;
    }

    public Integer getMicType() {
        return micType;
    }

    public void setMicType(Integer micType) {
        this.micType = micType;
    }

    public Integer getGuildId() {
        return guildId;
    }

    public void setGuildId(Integer guildId) {
        this.guildId = guildId;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}