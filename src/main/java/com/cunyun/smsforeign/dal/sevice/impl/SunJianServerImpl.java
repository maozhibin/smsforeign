package com.cunyun.smsforeign.dal.sevice.impl;

import com.alibaba.druid.sql.visitor.functions.Substring;
import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.cunyun.smsforeign.common.*;
import com.cunyun.smsforeign.dal.ReqBody;
import com.cunyun.smsforeign.dal.dao.SmsPlatformMapper;
import com.cunyun.smsforeign.dal.dao.SmsSendDetailsMapper;
import com.cunyun.smsforeign.dal.dao.SmsSendTaskMapper;
import com.cunyun.smsforeign.dal.model.Account;
import com.cunyun.smsforeign.dal.model.SmsPlatform;
import com.cunyun.smsforeign.dal.model.SmsSendDetails;
import com.cunyun.smsforeign.dal.model.SmsSendTask;
import com.cunyun.smsforeign.dal.sevice.SunJianServer;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.*;

/**
 * 笋尖平台实现类
 */
@Service
@Slf4j
public class SunJianServerImpl implements SunJianServer {
    @Resource
    private ConfigurablePropAccessorUtils propAccessorUtils;
    @Resource
    private SmsSendTaskMapper smsSendTaskMapper;
    @Resource
    private SmsSendDetailsMapper smsSendDetailsMapper;
    @Resource
    private SmsPlatformMapper smsPlatformMapper;

    //普通短信发送
    @Override
    public void smsCharactersSend(Account account,JsonResponseMsg result, ReqBody reqBody) {
        String url = propAccessorUtils.getProperties("c_sunjian_send_video_templet_url");
        String loginname = propAccessorUtils.getProperties("sunjian_loginname");
        String password = propAccessorUtils.getProperties("sunjian_pwd");
        String txtPath = propAccessorUtils.getProperties("video_txt");
//        String content = reqBody.getContent();
        //有用笋尖普通短信支持三网通所以直接拿来用不用区分运营商
        Boolean resultBoolen = filePost(url,loginname,password,result,reqBody,txtPath);
        if(resultBoolen){
            insertSql(reqBody,result,account,SmsPlatformCode.SUN_JIAN_CODE,1);
        }
    }

    public void insertSql(ReqBody reqBody,JsonResponseMsg result,Account account,String code,Integer smsType){
        String mobileid = reqBody.getMobile();
            Date date = new Date();
            List<SmsSendDetails> list = new ArrayList<>();
            SmsPlatform smsPlatform =  smsPlatformMapper.queryByCode(code,smsType);
            String[] mobiles = mobileid.split(",");
            for (String s:mobiles) {
                SmsSendDetails smsSendDetails = new SmsSendDetails();
                smsSendDetails.setAdminId(account.getAccountId());
                smsSendDetails.setCreateTime(date);
                if(reqBody.getType().equals("2")){//视频类型需要审核所以设置为暂时未发送状态，且需要自己下发
                    smsSendDetails.setIsSend(0);
                }else if(reqBody.getType().equals("1")){//短信的话虽然要审核，但审核完后笋尖会自动下发，所以就认为下发了
                    smsSendDetails.setIsSend(1);
                }
                smsSendDetails.setAssessor(1);
                smsSendDetails.setCharacteristic(reqBody.getCharacteristic());
                smsSendDetails.setIsEmploy(0);
                smsSendDetails.setContent(reqBody.getContent());
                smsSendDetails.setMobile(s);
                smsSendDetails.setSupplierCode(code);
                smsSendDetails.setCost(smsPlatform.getPrice());

                list.add(smsSendDetails);
        }
        smsSendDetailsMapper.insertByBatch(list);

    }

    public  Boolean filePost(String url, String loginname, String password,JsonResponseMsg result,ReqBody reqBody,String txtPath){
        //1:创建一个httpclient对象
        Boolean resultBoolen = true;
        HttpClient httpclient = new DefaultHttpClient();
        Charset charset = Charset.forName("gbk");//设置编码
        try {
            //2：创建http的发送方式对象，是GET还是post
            HttpPost httppost = new HttpPost(url);
            //3：创建要发送的实体，就是key-value的这种结构
            MultipartEntity reqEntity = new MultipartEntity();
            StringBody name = name = new StringBody(loginname,charset);
            StringBody pwd = new StringBody(password,charset);
            StringBody body = new StringBody(reqBody.getContent(),charset);
            StringBody tel = new StringBody(reqBody.getMobile(),charset);
            StringBody uuid = new StringBody(UUID.randomUUID().toString().replaceAll("-", ""),charset);
            reqEntity.addPart("flowid", uuid);
            reqEntity.addPart("loginname", name);
            reqEntity.addPart("password", pwd);
            reqEntity.addPart("content", body);
            reqEntity.addPart("mobileid", tel);
            //如果是视频短信的话需要以下操作
            if(reqBody.getType().equals("2")){
                FileBody videoFile = new FileBody(new File(reqBody.getVideo()));
                FileBody picFile = new FileBody(new File(reqBody.getPicture()));
                //txt文件生成
                TxtExport.creatTxtFile(txtPath,reqBody.getTitle());
                TxtExport.writeTxtFile(txtPath,reqBody.getTitle(),reqBody.getContent()+"温馨提示：本条短信免收流量费【视频短信】");
                String filenameTemp = txtPath + reqBody.getTitle() + ".txt";

                FileBody txtFile = new FileBody(new File(filenameTemp));
                reqEntity.addPart("video", videoFile);
                reqEntity.addPart("pic", picFile);
                reqEntity.addPart("txt", txtFile);

                StringBody title = new StringBody(reqBody.getTitle(),charset);
                reqEntity.addPart("msg_title", title);

                StringBody sign = new StringBody("浙江完美在线",charset);
                reqEntity.addPart("msg_sign", sign);
            }

            httppost.setEntity(reqEntity);
            //4：执行httppost对象，从而获得信息
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity resEntity = response.getEntity();
            //获得返回来的信息，转化为字符串string
            String resString = EntityUtils.toString(resEntity);


            if(reqBody.getType().equals("1")){
                if("0".equals(resString)){
                    result.setCode(200);
                    result.setMsgId("");
                    result.setMsg("已经发送到运营商那边，审核完后会自动下发");
                }
            }else if(reqBody.getType().equals("2")) {
                JSONObject object = new JSONObject();
                JSONObject object1  = (JSONObject) object.parse(resString);
                if("0".equals(object1.getString("code"))){
                    if(!org.springframework.util.StringUtils.isEmpty(result.getMsgId())){
                        result.setMsgId(result.getMsgId()+","+object1.getString("tmpeId"));
                    }else{
                        result.setMsgId(object1.getString("tmpeId"));
                    }
                    result.setCode(200);
                    result.setMsg("已经发送到运营商那边，审核完后会自动下发");
                    reqBody.setCharacteristic(object1.getString("tmpeId"));
                }else{
                    result.setCode(-101);
                    result.setMsgId("");
                    result.setMsg("调取移动运营商接口异常");
                    resultBoolen=false;
                }
            }
        } catch (UnsupportedEncodingException e) {
            resultBoolen=false;
            log.error("code=-101 ,笋间调取短信接口错误");
            result.setCode(-101);
            result.setMsgId("");
            result.setMsg("调取移动运营商接口异常");
            e.printStackTrace();
        } catch (IllegalStateException e) {
            resultBoolen=false;
            log.error("code=-101 ,笋间调取短信接口错误");
            result.setCode(-101);
            result.setMsgId("");
            result.setMsg("调取移动运营商接口异常");
            e.printStackTrace();
        } catch (IOException e) {
            resultBoolen=false;
            log.error("code=-101,笋间调取短信接口错误");
            result.setCode(-101);
            result.setMsgId("");
            result.setMsg("调取移动运营商接口异常");
            e.printStackTrace();
        } finally {
            try { httpclient.getConnectionManager().shutdown(); } catch (Exception ignore) {
            }
        }
        return resultBoolen;
    }

    /**
     * 视频短信模板申请
     * @param account
     * @param result
     * @param reqBody
     */
    @Override
    public void smsVideoApply(Account account, JsonResponseMsg result, ReqBody reqBody) {


//        Boolean isLawful=isLawful(reqBody,result);
//        if(!isLawful){
//            return;
//        }
        //先申请模板
        String url = propAccessorUtils.getProperties("c_sunjian_apply_video_templet_url");
        String loginname = propAccessorUtils.getProperties("sunjian_loginname");
        String password = propAccessorUtils.getProperties("sunjian_pwd");
        String txtPath = propAccessorUtils.getProperties("video_txt");
        filePost(url,loginname,password,result,reqBody,txtPath);
        try {
            TxtExport.delete(txtPath,reqBody.getTitle());
        } catch (IOException e) {
            log.error("code=-101,删除临时txt文件错误");
            result.setCode(-101);
            result.setMsgId("");
            result.setMsg("删除临时txt文件错误");
            e.printStackTrace();
            e.printStackTrace();
        }
        insertSql(reqBody,result,account,SmsPlatformCode.SUN_JIAN_CODE,2);
    }


//    //判断彩信内容是否合法
//    public static Boolean isLawful(ReqBody reqBody,JsonResponseMsg result) {
//        //判断视频格式
//        File tempFile =new File(reqBody.getVideo());
//        String fileName = tempFile.getName();
//        String gehsi =  fileName.substring(fileName.length()-3,fileName.length());
//        if(!"3gp".equals(gehsi) && !"mp4".equals(gehsi)){
//            result.setMsg("视频格式不对,请传入mp4，或者3gp格式的");
//            result.setCode(-101);
//            result.setMsgId("");
//            return false;
//        }
//
//        //计算彩信内容长度大小不能大于1.99M
//        Long length = null;
//        try {
//            length = Utils.getFileSize(new File(reqBody.getVideo()));
//            if(!StringUtils.isEmpty(reqBody.getPicture())){
//                length +=Utils.getFileSize(new File(reqBody.getPicture()));
//                String picType = Utils.getPicType(new FileInputStream(new File(reqBody.getPicture())));
//                if(!CommonConstants.TYPE_GIF.equals(picType)&&!CommonConstants.TYPE_JPG.equals(picType)&&!CommonConstants.TYPE_PNG.equals(picType)){
//                    result.setMsg("你的图片格式不对，请传入jpg,png,jif格式的");
//                    result.setCode(-101);
//                    result.setMsgId("");
//                    return false;
//                }
//            }
//            if(CommonConstants.SMS_VIDEO_MAX_LENGTH < length){
//                result.setMsg("你的短信内容太大了");
//                result.setCode(-101);
//                result.setMsgId("");
//                return false;
//            }
//        } catch (IOException e) {
//            result.setMsg("获取文件大小长度错误");
//            result.setCode(-101);
//            result.setMsgId("");
//            e.printStackTrace();
//            return false;
//        }
//        return  true;
//    }
}
