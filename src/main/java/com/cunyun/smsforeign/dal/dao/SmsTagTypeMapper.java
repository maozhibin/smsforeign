package com.cunyun.smsforeign.dal.dao;

import com.cunyun.smsforeign.dal.model.SmsTagType;

public interface SmsTagTypeMapper {
    int insert(SmsTagType record);

    int insertSelective(SmsTagType record);

    SmsTagType selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SmsTagType record);

    int updateByPrimaryKey(SmsTagType record);
}