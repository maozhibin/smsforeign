package com.cunyun.smsforeign.dal.dao;

import com.cunyun.smsforeign.dal.model.Account;
import org.apache.ibatis.annotations.Param;

public interface AccountMapper {
    int insert(Account record);

    int insertSelective(Account record);

    Account selectByPrimaryKey(Integer accountId);

    int updateByPrimaryKeySelective(Account record);

    int updateByPrimaryKey(Account record);

    Account queryByUserName(@Param("userName") String userName);
}