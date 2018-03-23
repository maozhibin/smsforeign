package com.cunyun.smsforeign.dal.dao;

import com.cunyun.smsforeign.dal.model.SmsSendRecord;

import java.util.List;

public interface SmsSendRecordMapper {
    void insertList(List<SmsSendRecord> list);
}
