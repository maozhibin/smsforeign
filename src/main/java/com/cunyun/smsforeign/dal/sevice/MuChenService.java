package com.cunyun.smsforeign.dal.sevice;

import com.cunyun.smsforeign.common.JsonResponseMsg;
import com.cunyun.smsforeign.dal.ReqBody;
import com.cunyun.smsforeign.dal.model.Account;

import java.util.List;

public interface MuChenService {
    void smsVideoApply(Account account, JsonResponseMsg result, ReqBody reqBody);
}
