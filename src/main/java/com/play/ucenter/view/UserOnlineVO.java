package com.play.ucenter.view;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by lenovo on 2020/3/26.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserOnlineVO implements Serializable {
    private static final long serialVersionUID = 1L;
    // fields
    /**
     * 用户ID
     **/
    private Long userId;
    /**
     * 用户登录凭证
     **/
    private String token;
    /**
     * 登录时间
     **/
    private Timestamp refreshTime;
    /**
     * 授权时间
     **/
    private Timestamp tokenTime;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Timestamp getRefreshTime() {
        return refreshTime;
    }

    public void setRefreshTime(Timestamp refreshTime) {
        this.refreshTime = refreshTime;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Timestamp getTokenTime() {
        return tokenTime;
    }

    public void setTokenTime(Timestamp tokenTime) {
        this.tokenTime = tokenTime;
    }
}