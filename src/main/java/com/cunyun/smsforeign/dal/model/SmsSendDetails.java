package com.cunyun.smsforeign.dal.model;

import java.math.BigDecimal;
import java.util.Date;

public class SmsSendDetails {
    private Integer id;

    private Integer adminId;

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getCreateTime() {

        return createTime;
    }

    private Date createTime;

    private Date taskSendTime;

    private Integer assessor;

    public void setAssessor(Integer assessor) {
        this.assessor = assessor;
    }

    public Integer getAssessor() {

        return assessor;
    }

    private Date realSendTime;

    private Integer isSend;

    private Integer isEmploy;

    private String mobile;

    private String content;

    @Override
    public String toString() {
        return "SmsSendDetails{" +
                "id=" + id +
                ", adminId=" + adminId +
                ", createTime=" + createTime +
                ", taskSendTime=" + taskSendTime +
                ", assessor=" + assessor +
                ", realSendTime=" + realSendTime +
                ", isSend=" + isSend +
                ", isEmploy=" + isEmploy +
                ", mobile='" + mobile + '\'' +
                ", content='" + content + '\'' +
                ", extVideoId=" + extVideoId +
                ", postTime=" + postTime +
                ", supplierCode='" + supplierCode + '\'' +
                ", characteristic='" + characteristic + '\'' +
                ", cost=" + cost +
                '}';
    }

    private Integer extVideoId;

    private Integer postTime;

    private String supplierCode;

    private String characteristic;

    public void setCharacteristic(String characteristic) {
        this.characteristic = characteristic;
    }

    public String getCharacteristic() {

        return characteristic;
    }

    private BigDecimal cost;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAdminId() {
        return adminId;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

    public Date getTaskSendTime() {
        return taskSendTime;
    }

    public void setTaskSendTime(Date taskSendTime) {
        this.taskSendTime = taskSendTime;
    }

    public Date getRealSendTime() {
        return realSendTime;
    }

    public void setRealSendTime(Date realSendTime) {
        this.realSendTime = realSendTime;
    }

    public Integer getIsSend() {
        return isSend;
    }

    public void setIsSend(Integer isSend) {
        this.isSend = isSend;
    }

    public Integer getIsEmploy() {
        return isEmploy;
    }

    public void setIsEmploy(Integer isEmploy) {
        this.isEmploy = isEmploy;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getExtVideoId() {
        return extVideoId;
    }

    public void setExtVideoId(Integer extVideoId) {
        this.extVideoId = extVideoId;
    }

    public Integer getPostTime() {
        return postTime;
    }

    public void setPostTime(Integer postTime) {
        this.postTime = postTime;
    }

    public String getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public BigDecimal getCost() {

        return cost;
    }
}