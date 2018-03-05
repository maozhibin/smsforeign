package com.cunyun.smsforeign.common;

import java.io.Serializable;
import java.util.Map;

/**
 * 接口响应实体
 */
public class JsonResponseMsg implements Serializable {

    private static final long serialVersionUID = -6067549589990462156L;
    public static final int CODE_SUCCESS = 200;
    public static final String MSG_SUCCESS = "success";

    public static final int CODE_FAIL = 1;
    public static final String MSG_FAIL = "调取运营商接口失败";

    public static final int CODE_WRONG_SIGN = -100;
    public static final String MSG_WRONG_SIGN = "签名错误";

    public static final int CODE_WRONG_PARAM = -101;
    public static final String MSG_WRONG_PARAM = "参数错误";

    public static final int CODE_LOGIN_INVALID = -200;
    public static final String MSG_LOGIN_INVALID = "未登陆或登陆状态失效,请重新登陆";

    public static final int CODE_ASSESSOR= 100;
    public static final String MSG_ASSESSOR= "短信正在审核中";


    private int code;// 响应代码
    private String msg;// 响应代码对应信息
    private String msgId;// 移动视频短信标识
    private Map<String, Object> object;// 响应对象数据

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }


    public String getMsgId() {

        return msgId;
    }

    public int getCode() {
        return code;

    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Map<String, Object> getObject() {
        return object;
    }

    public void setObject(Map<String, Object> object) {
        this.object = object;
    }

    public JsonResponseMsg fill(int code, String msg,String msgId) {
        this.setCode(code);
        this.setMsg(msg);
        this.setMsgId(msgId);
        return this;
    }
    public JsonResponseMsg fill(int code, String msg,Map<String, Object> object) {
        this.setCode(code);
        this.setMsg(msg);
        this.setObject(object);
        return this;
    }

    public JsonResponseMsg fill(int code, String msg, Map<String, Object> object,String msgId) {
        this.fill(code, msg,msgId);
        this.setObject(object);
        return this;
    }

}

