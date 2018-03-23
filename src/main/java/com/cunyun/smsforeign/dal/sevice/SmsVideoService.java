package com.cunyun.smsforeign.dal.sevice;

import com.cunyun.smsforeign.common.JsonResponseMsg;
import com.cunyun.smsforeign.dal.ReqBody;
import com.cunyun.smsforeign.dal.model.Account;
import com.cunyun.smsforeign.dal.model.SmsPlatform;

import java.util.List;

public interface SmsVideoService {
    void smsVideoSend(Account account, JsonResponseMsg result, ReqBody reqBody, List<SmsPlatform> smsPlatformsVideo);
}
