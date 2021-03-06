package com.cunyun.smsforeign.dal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReqBody {
    private String sign;

    private String title;

    private String mobile;

    private String type;

    private String video;

    private String picture;

    private String userName;

    private String password;

    private String content;

    private String characteristic;

    private String smsType;

    private String ip;
}
