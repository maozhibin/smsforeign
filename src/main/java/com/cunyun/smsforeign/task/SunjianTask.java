package com.cunyun.smsforeign.task;

import com.alibaba.fastjson.JSONObject;
import com.cunyun.smsforeign.common.ConfigurablePropAccessorUtils;
import com.cunyun.smsforeign.common.JsonResponseMsg;
import com.cunyun.smsforeign.common.SmsPlatformCode;
import com.cunyun.smsforeign.dal.ReqBody;
import com.cunyun.smsforeign.dal.dao.SmsSendDetailsMapper;
import com.cunyun.smsforeign.dal.model.SmsSendDetails;
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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
    @Scheduled(cron = "0 0/5 * * * ?")//五分钟
//    @Scheduled(cron = "0 0/1 * * * ?")//每分钟
    public void cronJob(){
        //视频短信相关数据
        String loginname = propAccessorUtils.getProperties("sunjian_loginname");
        String password = propAccessorUtils.getProperties("sunjian_pwd");
        String queryurl = propAccessorUtils.getProperties("c_sunjian_query_video_templet_url");
        String sendurl = propAccessorUtils.getProperties("c_sunjian_send_video_templet_url");

        //普通短信接口相关数据
        String loginnameCharacters = propAccessorUtils.getProperties("sunjian_characters_loginname");
        String passwordCharacters = propAccessorUtils.getProperties("sunjian_characters_pwd");
        String queryurlCharacters = propAccessorUtils.getProperties("sunjian_characters_queryMsgTemplate");

        //查询还没有下发的任务
        List<String> characteristics = smsSendDetailsMapper.queryNoSendSmsCharacteristic(SmsPlatformCode.SUN_JIAN_CODE);
        for (String characteristic:characteristics) {
            if(!StringUtils.isEmpty(characteristic)){
                //如果成功，code 为 0 ，对应state为当前模板的状态 0为审核中 1成功 2失败
                SmsSendDetails smsSendDetails = smsSendDetailsMapper.queryBycharacteristicAndCode(characteristic,SmsPlatformCode.SUN_JIAN_CODE);
                Integer type =smsSendDetails.getExtVideoId();
                String result="";
                if(type==1){//文字短信查询
                    result=query(characteristic,loginnameCharacters,passwordCharacters,queryurlCharacters);
                }else if(type==2){//视频短信查询
                    result=query(characteristic,loginname,password,queryurl);
                }
                JSONObject object = new JSONObject();
                JSONObject object1  = (JSONObject) object.parse(result);
                System.out.println("object1:"+object1);
                String code =  object1.getString("code");
                String state =  object1.getString("state");
                if("0".equals(code)){//查询结果成功
                    if("1".equals(state)){//如果状态为审核成功做下发处理
//                        String send = "";
//                        if(type==1){//文字短信下发
//                            send=send(sendurl,loginnameCharacters,passwordCharacters,characteristic,smsSendDetails.getMobile());
//                        }else if(type==2){//视频短信下发
//                            send=send(sendurl,loginname,password,characteristic,smsSendDetails.getMobile());
//                        }
//                        if("0".equals(send)){
                            smsSendDetailsMapper.updatebByCharacteristicAndCode(characteristic,SmsPlatformCode.SUN_JIAN_CODE);
                        }
                    }
                }else{
                    log.error("查询笋尖短信模板审核接口失败");
                    return;
                }
            }
        }
    //查询模板状态
    public static String query(String templateId,String loginname,String password,String queryurl){
        //1:创建一个httpclient对象
        HttpClient httpclient = new DefaultHttpClient();
        Charset charset = Charset.forName("gbk");//设置编码
        String result = "";
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
             result = EntityUtils.toString(resEntity);
             System.out.println("templateId:"+templateId+","+result);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try { httpclient.getConnectionManager().shutdown(); } catch (Exception ignore) {}
        }
        return result;
    }


//    /**
//     * 短信下发
//     */
//
//    public static String send(String sendUrl,String loginname,String password,String content,String mobileid){
//        String resString ="";
//        String arrs[] = mobileid.split(",");
//        for (String arr:arrs) {
//            //1:创建一个httpclient对象
//            HttpClient httpclient = new DefaultHttpClient();
//            Charset charset = Charset.forName("gbk");//设置编码
//            try {
//                //2：创建http的发送方式对象，是GET还是post
//                HttpPost httppost = new HttpPost(sendUrl);
//                //3：创建要发送的实体，就是key-value的这种结构
//                MultipartEntity reqEntity = new MultipartEntity();
//                StringBody name = new StringBody(loginname,charset);//填写分配帐户名
//                StringBody pwd = new StringBody(password,charset);//填写分配密码
//                StringBody contentValue = new StringBody(content,charset);
//                StringBody mobileids = new StringBody(arr,charset);
//                StringBody flowid = new StringBody(UUID.randomUUID().toString().replaceAll("-", ""),charset);
//                reqEntity.addPart("loginname", name);
//                reqEntity.addPart("password", pwd);
//                reqEntity.addPart("content", contentValue);
//                reqEntity.addPart("mobileid", mobileids);
//                reqEntity.addPart("flowid", flowid);
//                httppost.setEntity(reqEntity);
//                //4：执行httppost对象，从而获得信息
//                HttpResponse response = httpclient.execute(httppost);
//                HttpEntity resEntity = response.getEntity();
//                //获得返回来的信息，转化为字符串string
//                resString = EntityUtils.toString(resEntity);
//            } catch (UnsupportedEncodingException e) {
//                log.error("调用移动发短信接口失败");
//                e.printStackTrace();
//            } catch (IllegalStateException e) {
//                log.error("调用移动发短信接口失败");
//                e.printStackTrace();
//            } catch (IOException e) {
//                log.error("调用移动发短信接口失败");
//                e.printStackTrace();
//            } finally {
//                try { httpclient.getConnectionManager().shutdown(); } catch (Exception ignore) {}
//            }
//        }
//        return resString;
//    }
}
