package com.cunyun.smsforeign.dal.sevice.impl;

import com.alibaba.fastjson.JSONObject;
import com.cunyun.smsforeign.common.*;
import com.cunyun.smsforeign.dal.ReqBody;
import com.cunyun.smsforeign.dal.dao.SmsPlatformMapper;
import com.cunyun.smsforeign.dal.dao.SmsSendDetailsMapper;
import com.cunyun.smsforeign.dal.dao.SmsSendTaskMapper;
import com.cunyun.smsforeign.dal.model.Account;
import com.cunyun.smsforeign.dal.model.SmsPlatform;
import com.cunyun.smsforeign.dal.model.SmsSendDetails;
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
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.File;
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

    //普通短信模板申请
    @Override
    public void smsCharactersSend(Account account,JsonResponseMsg result, ReqBody reqBody) {
        String url = propAccessorUtils.getProperties("sunjian_characters_getMsgTemplate");
        String loginname = propAccessorUtils.getProperties("sunjian_characters_loginname");
        String password = propAccessorUtils.getProperties("sunjian_characters_pwd");
//        String content = reqBody.getContent();
        //有用笋尖普通短信支持三网通所以直接拿来用不用区分运营商
        Boolean resultBoolen = filePost(url,loginname,password,result,reqBody,"");
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
                smsSendDetails.setIsSend(0);
                if("2".equals(reqBody.getType())){
                    smsSendDetails.setExtVideoId(2);
                }else if("1".equals(reqBody.getType())){
                    smsSendDetails.setExtVideoId(1);
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

    /**
     * 短信模板申请
     * @param url
     * @param loginname
     * @param password
     * @param result
     * @param reqBody
     * @param txtPath
     * @return
     */
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
            StringBody content =null;
            if(!StringUtils.isEmpty(reqBody.getContent())){
                 content = new StringBody(reqBody.getContent(),charset);
            }
            reqEntity.addPart("loginname", name);
            reqEntity.addPart("password", pwd);
            if("1".equals(reqBody.getType())){
                reqEntity.addPart("msg_content", content);
            }
            //如果是视频短信的话需要以下操作
            if("2".equals(reqBody.getType())){
                //视频
                FileBody videoFile = new FileBody(new File(reqBody.getVideo()));
                reqEntity.addPart("video", videoFile);
                //图片
                if(!org.apache.commons.lang3.StringUtils.isEmpty(reqBody.getPicture())){
                    FileBody picFile = new FileBody(new File(reqBody.getPicture()));
                    reqEntity.addPart("pic", picFile);
                }
                //内容txt文件生成
                TxtExport.creatTxtFile(txtPath,reqBody.getTitle());
                if(StringUtils.isEmpty(reqBody.getContent())){
                    TxtExport.writeTxtFile(txtPath,reqBody.getTitle(),"温馨提示：本条短信免收流量费");
                }else{
                    TxtExport.writeTxtFile(txtPath,reqBody.getTitle(),reqBody.getContent()+"温馨提示：本条短信免收流量费");
                }
                String filenameTemp = txtPath+ reqBody.getTitle() + ".txt";
                FileBody txtFile = new FileBody(new File(filenameTemp));
                reqEntity.addPart("txt", txtFile);
                //标题
                StringBody title = new StringBody(reqBody.getTitle(),charset);
                reqEntity.addPart("msg_title", title);
                //签名
                StringBody sign = new StringBody("浙江完美在线",charset);
                reqEntity.addPart("msg_sign", sign);
            }

            httppost.setEntity(reqEntity);
            //4：执行httppost对象，从而获得信息
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity resEntity = response.getEntity();
            //获得返回来的信息，转化为字符串string
            String resString = EntityUtils.toString(resEntity);
            JSONObject object = new JSONObject();
            JSONObject object1  = (JSONObject) object.parse(resString);
            if("0".equals(object1.getString("code"))){
                if(reqBody.getType().equals("2")){
                    if(!org.springframework.util.StringUtils.isEmpty(result.getMsgId())){
                        result.setMsgId(result.getMsgId()+","+object1.getString("tmpeId"));
                    }else{
                        result.setMsgId(object1.getString("tmpeId"));
                    }
                    reqBody.setCharacteristic(object1.getString("tmpeId"));
                }else if (reqBody.getType().equals("1")){
                    System.out.println(object1);
                    if(!org.springframework.util.StringUtils.isEmpty(result.getMsgId())){
                        result.setMsgId(result.getMsgId()+","+object1.getString("msgId"));
                    }else{
                        result.setMsgId(object1.getString("msgId"));
                    }
                    reqBody.setCharacteristic(object1.getString("msgId"));
                }
                result.setCode(200);
                result.setMsg("已经发送到运营商那边，审核完后会自动下发");
            }else{
                result.setCode(-101);
                result.setMsgId("");
                result.setMsg("调取移动运营商接口异常");
                resultBoolen=false;
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


}
