package com.cunyun.smsforeign.dal.dao;

import com.cunyun.smsforeign.dal.model.LoginLogs;

public interface LoginLogsMapper {
    int insert(LoginLogs record);

    int insertSelective(LoginLogs record);

    LoginLogs selectByPrimaryKey(Integer loginId);

    int updateByPrimaryKeySelective(LoginLogs record);

    int updateByPrimaryKey(LoginLogs record);
}