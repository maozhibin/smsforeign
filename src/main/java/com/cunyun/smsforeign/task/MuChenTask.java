package com.cunyun.smsforeign.task;

import com.alibaba.fastjson.JSONObject;
import com.cunyun.smsforeign.common.ConfigurablePropAccessorUtils;
import com.cunyun.smsforeign.common.SmsPlatformCode;
import com.cunyun.smsforeign.common.muchen.HttpClientUtil;
import com.cunyun.smsforeign.common.muchen.MD5;
import com.cunyun.smsforeign.common.muchen.MuChenUtil;
import com.cunyun.smsforeign.dal.dao.SmsSendDetailsMapper;
import com.cunyun.smsforeign.dal.model.SmsSendDetails;
import com.cunyun.smsforeign.dal.sevice.MuChenService;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;
import java.util.UUID;

@Component
@Slf4j
public class MuChenTask {
    @Resource
    private SmsSendDetailsMapper smsSendDetailsMapper;
    @Resource
    private MuChenService muChenService;
    @Resource
    private ConfigurablePropAccessorUtils propAccessorUtils;


    /**
     * 牧尘视频短信接口查询下发
     */



//    @Scheduled(cron="0 0 0/1 * * ?")//一个小时
//    @Scheduled(cron = "0 0/1 * * * ?")//每分钟
    @Scheduled(cron = "0 0/5 * * * ?")//五分钟
    public void cronJob(){
        String key = propAccessorUtils.getProperties("mucheng_key");
        String username = propAccessorUtils.getProperties("mucheng_username");
        String viewUrl = propAccessorUtils.getProperties("mucheng_view");
        String sendUrl = propAccessorUtils.getProperties("mucheng_sen");
        List<String> characteristics = smsSendDetailsMapper.queryNoSendSmsCharacteristic(SmsPlatformCode.MU_CHEN_CODE);
        for (String characteristic:characteristics) {
            TaskModel model = new TaskModel();
            model.setCharacteristic(characteristic);
            view(key,viewUrl,username,model);//查询标识id中的短信是否审核完毕
            if("0".equals(model.getCode())){//如果返回的是0则审核完毕
                //查询有多少个用户需要进行发送
                List<SmsSendDetails> smsSendDetailss = smsSendDetailsMapper.queryByCharacteristic(characteristic,SmsPlatformCode.MU_CHEN_CODE);
                for (SmsSendDetails smsSendDetail:smsSendDetailss) {//下发动作
                    if(!StringUtils.isEmpty(smsSendDetail.getMobile())){
                        model.setMobile(smsSendDetail.getMobile());
                        send(key,sendUrl, username, model);
                    }
                }
                smsSendDetailsMapper.updatebByCharacteristicAndCode(characteristic,SmsPlatformCode.MU_CHEN_CODE);
            }
        }
//        System.out.println("开始");
    }

    /**
     * 牧尘单个查询接口
     * @param key
     * @param url
     * @param username
     * @param model
     */
    public void view(String key,String url,String username,TaskModel model){
        Gson gson = new Gson();
        try{
            String sign =  key + "/api/view?";
            TreeMap<String, String> t = new TreeMap<String, String>();
            t.put("username", username);//用户名
            t.put("id", model.getCharacteristic());
            t.put("timestamp", new Date().getTime()+"");
            String _sign = MuChenUtil.createSign(t, sign);
            sign = MD5.str2MD5(_sign);
            t.put("sign", sign);
            String paraData = gson.toJson(t);
            HttpClientUtil httpClient = HttpClientUtil.getInstance();
            String html = httpClient.getResponseBodyAsString(url, paraData);
            JSONObject object = new JSONObject();
            JSONObject object1  = (JSONObject) object.parse(html);
            model.setCode(object1.getString("result"));
        }catch(Exception e){
            log.error("调取运营商查询接口失败");
            e.getStackTrace();
        }

    }

    /**
     * 牧尘短信下发方法
     * @param key
     * @param url
     * @param username
     * @param model
     */
    public void send(String key,String url,String username,TaskModel model) {
        Gson gson = new Gson();
        try {
            //用户接口秘钥 + 参数url
            String sign =  key + "/api/send?";
            TreeMap<String, String> t = new TreeMap<String, String>();
            t.put("username", username);//用户名
            t.put("sequenceNumber", get24UUID());
            t.put("userNumber",model.getMobile());
            t.put("id", "358992");
            t.put("timestamp", new Date().getTime()+"");
//            TreeMap<String, String> data = new TreeMap<String, String>();
//            data.put("name","AAA");
//            data.put("msg","BBB");
//            String param = gson.toJson(data);
//            t.put("param", param);
            String _sign = MuChenUtil.createSign(t, sign);
            sign = MD5.str2MD5(_sign);
            t.put("sign", sign);
            String paraData = gson.toJson(t);
            HttpClientUtil httpClient = HttpClientUtil.getInstance();
            httpClient.getResponseBodyAsString(url, paraData);
        } catch (Exception e) {
            e.getStackTrace();
        }

    }

    public static String get24UUID(){
        UUID id=UUID.randomUUID();
        String[] idd=id.toString().split("-");
        return idd[0]+idd[1]+idd[4];
    }
}
