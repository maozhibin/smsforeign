package com.cunyun.smsforeign.dal.dao;

import com.cunyun.smsforeign.dal.model.Contact;

public interface ContactMapper {
    int insert(Contact record);

    int insertSelective(Contact record);

    Contact selectByPrimaryKey(Integer contactId);

    int updateByPrimaryKeySelective(Contact record);

    int updateByPrimaryKeyWithBLOBs(Contact record);

    int updateByPrimaryKey(Contact record);
}