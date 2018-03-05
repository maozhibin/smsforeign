package com.cunyun.smsforeign.common;

import lombok.Getter;

/**
 * 短信平台相应码
 */
@Getter
public enum SmsPlatfromResCode {
    SUCCESS("S000", "请求成功"),
    NO_CHANNEL("S001", "账号或者密码错误"),
    DECRYPT_JSON_ERR("S008", "加密信息错误"),
    REQ_JSON_ERR("S009", "请求参数错误"),
    ERROR("S999", "系统异常"),;

    private String code;
    private String msg;

    SmsPlatfromResCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}

