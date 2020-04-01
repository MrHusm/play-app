package com.play.im.view;

import java.util.Date;

public class ChatroomVO {
    /**
     * 聊天室ID
     */
    private Integer roomId;

    /**
     * 聊天室名称
     */
    private String name;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}