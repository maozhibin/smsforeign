package com.cunyun.smsforeign.dal.model;

import java.math.BigDecimal;
import java.util.Date;

public class SmsPlatform {
    private Integer id;

    private String name;

    private BigDecimal price;

    private Integer supportMobile;

    private Integer smsType;

    private Integer wight;

    private Integer isStart;

    private Integer isEmploy;

    private String supplierCode;

    private Date updatedTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getSupportMobile() {
        return supportMobile;
    }

    public void setSupportMobile(Integer supportMobile) {
        this.supportMobile = supportMobile;
    }

    public Integer getSmsType() {
        return smsType;
    }

    public void setSmsType(Integer smsType) {
        this.smsType = smsType;
    }

    public Integer getWight() {
        return wight;
    }

    public void setWight(Integer wight) {
        this.wight = wight;
    }

    public Integer getIsStart() {
        return isStart;
    }

    public void setIsStart(Integer isStart) {
        this.isStart = isStart;
    }

    public Integer getIsEmploy() {
        return isEmploy;
    }

    public void setIsEmploy(Integer isEmploy) {
        this.isEmploy = isEmploy;
    }

    public String getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", name=").append(name);
        sb.append(", price=").append(price);
        sb.append(", supportMobile=").append(supportMobile);
        sb.append(", smsType=").append(smsType);
        sb.append(", wight=").append(wight);
        sb.append(", isStart=").append(isStart);
        sb.append(", isEmploy=").append(isEmploy);
        sb.append(", supplierCode=").append(supplierCode);
        sb.append(", updatedTime=").append(updatedTime);
        sb.append("]");
        return sb.toString();
    }
}