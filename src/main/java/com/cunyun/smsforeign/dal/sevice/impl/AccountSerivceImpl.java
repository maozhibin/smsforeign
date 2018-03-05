package com.cunyun.smsforeign.dal.sevice.impl;

import com.cunyun.smsforeign.dal.dao.AccountMapper;
import com.cunyun.smsforeign.dal.model.Account;
import com.cunyun.smsforeign.dal.sevice.AccountSerivce;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service
public class AccountSerivceImpl implements AccountSerivce {

    @Resource
    private AccountMapper accountMapper;
    @Override
    public Account queryByUserName(String userName) {
        return accountMapper.queryByUserName(userName);
    }
}
