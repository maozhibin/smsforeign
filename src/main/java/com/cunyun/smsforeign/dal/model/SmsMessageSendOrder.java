package com.cunyun.smsforeign.dal.model;

import java.util.Date;

public class SmsMessageSendOrder {
    private Integer id;

    private Integer tagId;

    private Integer channelId;

    private Integer customerId;

    private String title;

    private String content;

    private String msgType;

    private String videoUrl;

    private String imageUrl;

    private String fileUrl;

    private Integer statue;

    private Integer isEmploy;

    private Date createdTime;

    private Date updatedTime;

    private String confirmContent;

    private String cause;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTagId() {
        return tagId;
    }

    public void setTagId(Integer tagId) {
        this.tagId = tagId;
    }

    public Integer getChannelId() {
        return channelId;
    }

    public void setChannelId(Integer channelId) {
        this.channelId = channelId;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public Integer getStatue() {
        return statue;
    }

    public void setStatue(Integer statue) {
        this.statue = statue;
    }

    public Integer getIsEmploy() {
        return isEmploy;
    }

    public void setIsEmploy(Integer isEmploy) {
        this.isEmploy = isEmploy;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    public String getConfirmContent() {
        return confirmContent;
    }

    public void setConfirmContent(String confirmContent) {
        this.confirmContent = confirmContent;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", tagId=").append(tagId);
        sb.append(", channelId=").append(channelId);
        sb.append(", customerId=").append(customerId);
        sb.append(", title=").append(title);
        sb.append(", content=").append(content);
        sb.append(", msgType=").append(msgType);
        sb.append(", videoUrl=").append(videoUrl);
        sb.append(", imageUrl=").append(imageUrl);
        sb.append(", fileUrl=").append(fileUrl);
        sb.append(", statue=").append(statue);
        sb.append(", isEmploy=").append(isEmploy);
        sb.append(", createdTime=").append(createdTime);
        sb.append(", updatedTime=").append(updatedTime);
        sb.append(", confirmContent=").append(confirmContent);
        sb.append(", cause=").append(cause);
        sb.append("]");
        return sb.toString();
    }
}