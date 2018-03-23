package com.cunyun.smsforeign.dal.sevice;

import com.cunyun.smsforeign.common.JsonResponseMsg;
import com.cunyun.smsforeign.dal.ReqBody;
import com.cunyun.smsforeign.dal.model.Account;

import java.util.List;

public interface SunJianServer {
    void smsCharactersSend(Account account,JsonResponseMsg result, ReqBody reqBody);

    void smsVideoApply(Account account, JsonResponseMsg result, ReqBody reqBody);

    String send(String sendurl, String loginnameCharacters, String passwordCharacters, String characteristic, String yd, Integer id);
}
