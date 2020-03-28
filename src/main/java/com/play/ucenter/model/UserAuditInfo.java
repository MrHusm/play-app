package com.play.ucenter.model;

import java.util.Date;

public class UserAuditInfo {
    /**
     * 编号
     */
    private Integer id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 审核类型 审核类型：1=昵称，2=签名，3=头像，4=相册，5=视频
     */
    private Integer auditType;

    /**
     * 审核内容
     */
    private String content;

    /**
     * 创建人员
     */
    private Long creator;

    /**
     * 创建时间
     */
    private Date createdDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getAuditType() {
        return auditType;
    }

    public void setAuditType(Integer auditType) {
        this.auditType = auditType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getCreator() {
        return creator;
    }

    public void setCreator(Long creator) {
        this.creator = creator;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
}