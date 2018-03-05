package com.cunyun.smsforeign.dal.dao;

import com.cunyun.smsforeign.dal.model.SmsTagFiles;

public interface SmsTagFilesMapper {
    int insert(SmsTagFiles record);

    int insertSelective(SmsTagFiles record);

    SmsTagFiles selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SmsTagFiles record);

    int updateByPrimaryKey(SmsTagFiles record);
}