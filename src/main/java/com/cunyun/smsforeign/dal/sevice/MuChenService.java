package com.cunyun.smsforeign.dal.sevice;

import com.cunyun.smsforeign.common.JsonResponseMsg;
import com.cunyun.smsforeign.dal.ReqBody;
import com.cunyun.smsforeign.dal.model.Account;
import com.cunyun.smsforeign.task.TaskModel;

import java.util.List;

public interface MuChenService {
    void smsVideoApply(Account account, JsonResponseMsg result, ReqBody reqBody);

    void send(String key, String sendUrl, String username, TaskModel taskModel, Integer id);
}
