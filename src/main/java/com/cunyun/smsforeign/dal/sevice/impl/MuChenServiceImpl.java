package com.cunyun.smsforeign.dal.sevice.impl;

import com.alibaba.fastjson.JSONObject;
import com.cunyun.smsforeign.common.ConfigurablePropAccessorUtils;
import com.cunyun.smsforeign.common.JsonResponseMsg;
import com.cunyun.smsforeign.common.SmsPlatformCode;
import com.cunyun.smsforeign.common.muchen.FileEntityUtil;
import com.cunyun.smsforeign.common.muchen.HttpClientUtil;
import com.cunyun.smsforeign.common.muchen.MD5;
import com.cunyun.smsforeign.common.muchen.MuChenUtil;
import com.cunyun.smsforeign.dal.ReqBody;
import com.cunyun.smsforeign.dal.dao.SmsPlatformMapper;
import com.cunyun.smsforeign.dal.dao.SmsSendDetailsMapper;
import com.cunyun.smsforeign.dal.dao.SmsSendTaskMapper;
import com.cunyun.smsforeign.dal.model.Account;
import com.cunyun.smsforeign.dal.sevice.MuChenService;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import sun.misc.BASE64Encoder;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.util.*;

@Slf4j
@Service
public class MuChenServiceImpl implements MuChenService {
    static String key = "911b946998985945d9aac1deb8da2312";//秘钥
    @Resource
    private ConfigurablePropAccessorUtils propAccessorUtils;
    @Resource
    private SmsSendTaskMapper smsSendTaskMapper;
    @Resource
    private SmsSendDetailsMapper smsSendDetailsMapper;
    @Resource
    private SmsPlatformMapper smsPlatformMapper;
    @Resource
    private  SunJianServerImpl sunJianServerImpl;

    @Override
    public void smsVideoApply(Account account, JsonResponseMsg result, ReqBody reqBody) {
        String key = propAccessorUtils.getProperties("mucheng_key");
        String username = propAccessorUtils.getProperties("mucheng_username");
        String dyCheckSaveUrl = propAccessorUtils.getProperties("mucheng_dyCheckSave");
        dyCheckSave(result,reqBody,key,username,dyCheckSaveUrl);
        if(result.getCode()==200){
            sunJianServerImpl.insertSql(reqBody,result,account, SmsPlatformCode.MU_CHEN_CODE,2);
        }
    }

    //牧尘视频短信申请模板
    public void dyCheckSave(JsonResponseMsg result,ReqBody reqBody,String key,String username,String dyCheckSaveUrl) {

        Gson gson = new Gson();
        try {
            String sign = key + "/api/dyCheckSave?";
            TreeMap<String, String> t = new TreeMap<String, String>();
            t.put("username", username);//用户名
            t.put("title", reqBody.getTitle());
            t.put("timestamp", new Date().getTime() + "");
            t.put("type", "2");

            /**************动态创建文件*******************/
            List<List<FileEntityUtil>> list = new ArrayList<List<FileEntityUtil>>();
            /****************设置一档,不做使用，牧尘短信接口需要这个********************/
            List<FileEntityUtil> one = new ArrayList<FileEntityUtil>();
            FileEntityUtil f1 = new FileEntityUtil();
            f1.setName("乐竞视频短信.txt");
            f1.setContent("你好,欢迎使用乐竞视频短信");
            one.add(f1);
            List<FileEntityUtil> two = new ArrayList<FileEntityUtil>();
            if(!StringUtils.isEmpty(reqBody.getContent())){
                FileEntityUtil f2 = new FileEntityUtil();
                f2.setName( reqBody.getTitle()+".txt");
                f2.setContent(reqBody.getContent());
                two.add(f2);
            }
            if(!StringUtils.isEmpty(reqBody.getPicture())){
                FileEntityUtil f3 = new FileEntityUtil();
                f3.setName(reqBody.getTitle()+".png");
                f3.setContent(MuChenUtil.encodeBase64File(reqBody.getPicture()));
                two.add(f3);

            }
            FileEntityUtil f4 = new FileEntityUtil();
            f4.setName(reqBody.getTitle()+".mp4");
            f4.setContent(MuChenUtil.encodeBase64File(reqBody.getVideo()));
            two.add(f4);
            list.add(one);
            list.add(two);

            String param = gson.toJson(list);
            t.put("param", param);
            String _sign = MuChenUtil.createSign(t, sign);
            sign = MD5.str2MD5(_sign);
            t.put("sign", sign);
            String paraData = gson.toJson(t);

            HttpClientUtil httpClient = HttpClientUtil.getInstance();
            Long time = new Date().getTime();
            String html = httpClient.getResponseBodyAsString(dyCheckSaveUrl, paraData);
            System.out.println("eeeee:"+(new Date().getTime()-time));

            if(!StringUtils.isEmpty(html)){
                JSONObject object = new JSONObject();
                JSONObject object1  = (JSONObject) object.parse(html);
                if("0".equals(object1.getString("result"))){
                    if(!org.springframework.util.StringUtils.isEmpty(result.getMsgId())){
                        reqBody.setCharacteristic(object1.getString("desc"));
                    }else{
                        result.setMsgId(object1.getString("desc"));
                    }
                    reqBody.setCharacteristic(object1.getString("desc"));
                    result.setCode(200);
                    result.setMsg("已经发送到运营商那边，审核完后会自动下发");
                }else{
                    result.setCode(1);
                    result.setMsg("调取联通运营商接口异常");
                }
            }
        } catch (Exception e) {
            e.getStackTrace();
        }

    }



}