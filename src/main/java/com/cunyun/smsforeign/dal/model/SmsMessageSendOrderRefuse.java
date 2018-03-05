package com.cunyun.smsforeign.dal.model;

import java.util.Date;

public class SmsMessageSendOrderRefuse {
    private Integer id;

    private Integer orderId;

    private String cause;

    private Integer isEmploy;

    private Date updatedTime;

    private Integer adminId;

    private String steps;

    private String confirmContent;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public Integer getIsEmploy() {
        return isEmploy;
    }

    public void setIsEmploy(Integer isEmploy) {
        this.isEmploy = isEmploy;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    public Integer getAdminId() {
        return adminId;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

    public String getSteps() {
        return steps;
    }

    public void setSteps(String steps) {
        this.steps = steps;
    }

    public String getConfirmContent() {
        return confirmContent;
    }

    public void setConfirmContent(String confirmContent) {
        this.confirmContent = confirmContent;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", orderId=").append(orderId);
        sb.append(", cause=").append(cause);
        sb.append(", isEmploy=").append(isEmploy);
        sb.append(", updatedTime=").append(updatedTime);
        sb.append(", adminId=").append(adminId);
        sb.append(", steps=").append(steps);
        sb.append(", confirmContent=").append(confirmContent);
        sb.append("]");
        return sb.toString();
    }
}