package com.cunyun.smsforeign.dal.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class SmsSendRecord {

    private Integer id;

    private String mobile;

    private Integer smsSendDetailsId;

    private Date createdTime;

    private Date updatedTime;



}
