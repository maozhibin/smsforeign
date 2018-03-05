package com.cunyun.smsforeign.dal.sevice;

import com.cunyun.smsforeign.dal.model.Account;

public interface AccountSerivce {
    Account queryByUserName(String userName);
}
