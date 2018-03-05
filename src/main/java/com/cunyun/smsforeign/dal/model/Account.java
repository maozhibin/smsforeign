package com.cunyun.smsforeign.dal.model;

import java.util.Date;

public class Account {
    private Integer accountId;

    private Integer contactId;

    private String username;

    private String pwdSalt;

    private String pwd;

    private Integer loginCount;

    private Integer status;

    private Date lastLoginTime;

    private Date createdTime;

    private Date updatedTime;

    private String company;

    private String address;

    private Integer balance;

    private Integer isSupperAdmin;

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public Integer getContactId() {
        return contactId;
    }

    public void setContactId(Integer contactId) {
        this.contactId = contactId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPwdSalt() {
        return pwdSalt;
    }

    public void setPwdSalt(String pwdSalt) {
        this.pwdSalt = pwdSalt;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public Integer getLoginCount() {
        return loginCount;
    }

    public void setLoginCount(Integer loginCount) {
        this.loginCount = loginCount;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
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

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getBalance() {
        return balance;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }

    public Integer getIsSupperAdmin() {
        return isSupperAdmin;
    }

    public void setIsSupperAdmin(Integer isSupperAdmin) {
        this.isSupperAdmin = isSupperAdmin;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", accountId=").append(accountId);
        sb.append(", contactId=").append(contactId);
        sb.append(", username=").append(username);
        sb.append(", pwdSalt=").append(pwdSalt);
        sb.append(", pwd=").append(pwd);
        sb.append(", loginCount=").append(loginCount);
        sb.append(", status=").append(status);
        sb.append(", lastLoginTime=").append(lastLoginTime);
        sb.append(", createdTime=").append(createdTime);
        sb.append(", updatedTime=").append(updatedTime);
        sb.append(", company=").append(company);
        sb.append(", address=").append(address);
        sb.append(", balance=").append(balance);
        sb.append(", isSupperAdmin=").append(isSupperAdmin);
        sb.append("]");
        return sb.toString();
    }
}