package com.cunyun.smsforeign.dal.dao;

import com.cunyun.smsforeign.dal.model.SmsTag;

public interface SmsTagMapper {
    int insert(SmsTag record);

    int insertSelective(SmsTag record);

    SmsTag selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SmsTag record);

    int updateByPrimaryKey(SmsTag record);
}