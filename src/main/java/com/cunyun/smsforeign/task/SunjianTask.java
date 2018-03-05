package com.cunyun.smsforeign.task;

import com.alibaba.fastjson.JSONObject;
import com.cunyun.smsforeign.common.ConfigurablePropAccessorUtils;
import com.cunyun.smsforeign.common.JsonResponseMsg;
import com.cunyun.smsforeign.common.SmsPlatformCode;
import com.cunyun.smsforeign.dal.ReqBody;
import com.cunyun.smsforeign.dal.dao.SmsSendDetailsMapper;
import com.cunyun.smsforeign.dal.sevice.impl.SunJianServerImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.List;

@Component
@Slf4j
public class SunjianTask {
    @Resource
    private SmsSendDetailsMapper smsSendDetailsMapper;
    @Resource
    private SunJianServerImpl sunJianServer;
    @Resource
    private ConfigurablePropAccessorUtils propAccessorUtils;

    /**
     * 笋尖视频短信查询模板情况接口，及下发短信
     */
//    @Scheduled(cron="0/5 * *  * * ?")//五分钟
//    @Scheduled(cron="0 0 0/1 * * ?")//一个小时
//    @Scheduled(cron="0 0/30 * * * ?")//半个小时
    public void cronJob(){
//        String loginname = propAccessorUtils.getProperties("sunjian_loginname");
//        String password = propAccessorUtils.getProperties("sunjian_pwd");
//        String queryurl = propAccessorUtils.getProperties("c_sunjian_query_video_templet_url");
//        String sendurl = propAccessorUtils.getProperties("c_sunjian_send_video_templet_url");
//        //查询还没有下发的任务
//        List<String> characteristics = smsSendDetailsMapper.queryNoSendSmsCharacteristic(SmsPlatformCode.SUN_JIAN_CODE);
//        for (String characteristic:characteristics) {
//            if(!StringUtils.isEmpty(characteristic)){
//                //如果成功，code 为 0 ，对应state为当前模板的状态 0为审核中 1成功 2失败
//                String result =filePost(characteristic,loginname,password,queryurl);
//                JSONObject object = new JSONObject();
//                JSONObject object1  = (JSONObject) object.parse(result);
//                String code =  object1.getString("code");
//                String state =  object1.getString("state");
//                if("0".equals(code)){//查询结果成功
//                    if("1".equals(state)){//如果状态为审核成功做下发处理
//                        JsonResponseMsg res = new JsonResponseMsg();
//                        ReqBody reqBody = new ReqBody();
//                        reqBody.setContent(characteristic);
//                        reqBody.setType("1");
//
//
//
//
//                        sunJianServer.filePost(sendurl,loginname,password,res,reqBody,null);
//                    }
//                }else{
//                    log.error("查询笋尖短信模板审核接口失败");
//                    return;
//                }
//
//
//
//            }
//
//        }


    }

    //查询模板状态
    public static String filePost(String templateId,String loginname,String password,String queryurl){
       ;
        //1:创建一个httpclient对象
        HttpClient httpclient = new DefaultHttpClient();
        Charset charset = Charset.forName("gbk");//设置编码
        try {
            //2：创建http的发送方式对象
            HttpPost httppost = new HttpPost(queryurl);
            //3：创建要发送的实体，就是key-value
            MultipartEntity reqEntity = new MultipartEntity();
            StringBody name = new StringBody(loginname,charset);//填写分配帐户名
            StringBody pwd = new StringBody(password,charset);//填写分配密码
            StringBody tempId = new StringBody(templateId,charset);
            reqEntity.addPart("loginname", name);
            reqEntity.addPart("password", pwd);
            reqEntity.addPart("templateId", tempId);
            httppost.setEntity(reqEntity);
            //4：执行httppost对象，从而获得信息
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity resEntity = response.getEntity();
            //获得返回来的信息，转化为字符串string
            String resString = EntityUtils.toString(resEntity);
            System.out.println(resString);
            JSONObject json = JSONObject.parseObject(resString);
            System.out.println(json.getString("code"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try { httpclient.getConnectionManager().shutdown(); } catch (Exception ignore) {}
        }
        return "";
    }


}
