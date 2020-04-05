package com.play.ucenter.view;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
 * Created by lenovo on 2020/3/26.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserMicVO implements Serializable {
    private static final long serialVersionUID = 1L;
    // fields
    /**
     * 用户ID
     **/
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
     * 第几麦位
     */
    private Integer position;

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

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }
}