package com.cunyun.smsforeign.controller;

import com.alibaba.fastjson.JSONObject;
import com.cunyun.smsforeign.common.*;
import com.cunyun.smsforeign.dal.ReqBody;
import com.cunyun.smsforeign.dal.dao.SmsSendDetailsMapper;
import com.cunyun.smsforeign.dal.model.Account;
import com.cunyun.smsforeign.dal.model.SmsPlatform;
import com.cunyun.smsforeign.dal.model.SmsSendDetails;
import com.cunyun.smsforeign.dal.sevice.AccountSerivce;
import com.cunyun.smsforeign.dal.sevice.SmsCharactersService;
import com.cunyun.smsforeign.dal.sevice.SmsPlatformService;
import com.cunyun.smsforeign.dal.sevice.SmsVideoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHttpEntityEnclosingRequest;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

@CrossOrigin
@RequestMapping("sms")
@Controller
@Slf4j
public class MainController {
    @Resource
    private ConfigurablePropAccessorUtils propAccessorUtils;
    @Resource
    private AccountSerivce accountSerivce;
    @Resource
    private SmsPlatformService SmsPlatformService;
    @Resource
    private SmsCharactersService SmsCharactersService;
    @Resource
    private SmsVideoService smsVideoService;
    @Resource
    private SmsSendDetailsMapper smsSendDetailsMapper;


//    @ResponseBody
//    @RequestMapping(value = "send" ,method = RequestMethod.POST)
//    public JsonResponseMsg service(HttpServletRequest request) {
//        JsonResponseMsg jsonResponseMsg = new JsonResponseMsg();
//        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
//        MultipartFile file = multipartRequest.getFile("video");
//        String filename = file.getOriginalFilename();
//        System.out.println(filename);
//        String name=request.getParameter("userName");  //用于用户验证
//        String pwd=request.getParameter("password");
//
//        String token = request.getParameter("password");
//        return jsonResponseMsg;
//    }

    @ResponseBody
    @RequestMapping(value = "send" ,method = RequestMethod.POST)
    public JsonResponseMsg send(HttpServletRequest request) {
        JsonResponseMsg result = new JsonResponseMsg();
        ReqBody reqBody = new ReqBody();
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile video = multipartRequest.getFile("video");
        if(video==null){
            return result.fill(JsonResponseMsg.CODE_FAIL,"请传入视频","");
        }
        reqBody.setContent(request.getParameter("content"));
        reqBody.setType(request.getParameter("type"));
        reqBody.setUserName(request.getParameter("userName"));
        reqBody.setMobile(request.getParameter("mobile"));
        reqBody.setPassword(request.getParameter("password"));
        reqBody.setTitle(request.getParameter("title"));
        reqBody.setSign(request.getParameter("sign"));
        if("2".equals(reqBody.getType())){
            reqBody.setVideo(fileUpload(propAccessorUtils.getProperties("sms_file"),video));
            MultipartFile picFile = multipartRequest.getFile("picFile");
            if(picFile!=null){
                reqBody.setPicture(fileUpload(propAccessorUtils.getProperties("sms_file"),picFile));
            }
        }
        result = serviceCenter(reqBody, request);
        return result;
    }


    public static  String fileUpload( String url ,MultipartFile file) {
        String filePath= "";
        // 判断文件是否为空
        if (!file.isEmpty()) {
            try {
                // 文件保存路径
                 filePath = url + "/"+file.getOriginalFilename();
                file.transferTo(new File(filePath));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return  filePath;
    }

//    //文件上传
//    public static JsonResponseMsg filePost(String fil){
//        JsonResponseMsg responseMsg = new JsonResponseMsg();
//        //1:创建一个httpclient对象
//        HttpClient httpclient = new DefaultHttpClient();
//        try {
//            //2：创建http的发送方式对象，是GET还是post
////            HttpPost httppost = new HttpPost("http://47.97.174.218:55670/sms/fileUpload");//服务器
//            HttpPost httppost = new HttpPost("http://127.0.0.1:55670/sms/fileUpload");//本地
//            //3：创建要发送的实体，就是key-value的这种结构
//            MultipartEntity file = new MultipartEntity();
//            FileBody videoFile = new FileBody(new File(fil));
//            file.addPart("file",videoFile);
//            httppost.setEntity(file);
//            //4：执行httppost对象，从而获得信息
//            HttpResponse response = httpclient.execute(httppost);
//            HttpEntity resEntity = response.getEntity();
//            //获得返回来的信息，转化为字符串string
//            String resString = EntityUtils.toString(resEntity);
//            JSONObject object = new JSONObject();
//            JSONObject object1  = (JSONObject) object.parse(resString);
//            if("200".equals(object1.getString("code"))){
//                responseMsg.setCode(200);
//                responseMsg.setMsg(object1.getString("msg"));
//            }
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        } catch (IllegalStateException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            try { httpclient.getConnectionManager().shutdown(); } catch (Exception ignore) {}
//        }
//        return responseMsg;
//    }


    /**
     * 请求处理中心
     */
    private JsonResponseMsg serviceCenter(ReqBody reqBody, HttpServletRequest request) {
        JsonResponseMsg result = new JsonResponseMsg();
        String ip = Utils.getIpAddr(request);
        reqBody.setIp(ip);
        //判断账号是否合法
        Account account = accountSerivce.queryByUserName(reqBody.getUserName());
        if(account==null || !account.getPwd().equals(reqBody.getPassword())){
           return  result.fill(JsonResponseMsg.CODE_FAIL,"账号或者密码错误","");
        }

        if(StringUtils.isEmpty(reqBody.getMobile())){
            return  result.fill(JsonResponseMsg.CODE_FAIL,"请输入手机号","");
        }
        //判断签名是否合法
        String sign = account.getPwd()+account.getUsername();
        if(!Md5.encrypt32(sign).equals(reqBody.getSign())){
           return  result.fill(JsonResponseMsg.CODE_FAIL,"签名错误","");
        }
        //根据要发的短信类型进行处理
        if(StringUtils.isEmpty(reqBody.getType())){
            return  result.fill(JsonResponseMsg.CODE_FAIL,"请选择短信要发送的短信类型","");
        }

        if(StringUtils.isEmpty(reqBody.getTitle())){
            return  result.fill(JsonResponseMsg.CODE_FAIL,"请输入你要发送的标题","");
        }

        if(StringUtils.isEmpty(reqBody.getVideo())){
            return  result.fill(JsonResponseMsg.CODE_FAIL,"请传入你要发送的视频","");
        }
        //手机号码判断归属运营商
        String mobile = reqBody.getMobile();
        String[] mobiles = mobile.split(",");
        List<String> LT = new ArrayList<>();
        List<String> YD = new ArrayList<>();
        List<String> DX = new ArrayList<>();

        for (String s:mobiles) {
            String mobileType = JudgeOperator.matchNum(s);
            if(mobileType.equals(CommonConstants.DX)){
                DX.add(s);
            }else if(mobileType.equals(CommonConstants.LT)){
                LT.add(s);
            }else if(mobileType.equals(CommonConstants.YD)){
                YD.add(s);
            }
        }

        if(reqBody.getType().equals("1")){ //普通短信
            if(StringUtils.isEmpty(reqBody.getContent())){
                return  result.fill(JsonResponseMsg.CODE_FAIL,"请输入你要发送的内容","");
            }
            List<SmsPlatform> smsPlatformsCharacters = SmsPlatformService.queryBySmsTypeAndIsEmploy(CommonConstants.SMS_COMMON);
            SmsCharactersService.smsCharacters(account,result,reqBody,smsPlatformsCharacters,LT,YD,DX);
        }else if(reqBody.getType().equals("2")){//视频短信
//            JsonResponseMsg fileMsg= filePost(reqBody.getVideo());
//            if(200!=fileMsg.getCode()){
//                result.setMsg("上传视频文件失败");
//                result.setCode(-101);
//                return result;
//            }
//            reqBody.setVideo(propAccessorUtils.getProperties("sms_file")+"/"+fileMsg.getMsg());
//            if(!StringUtils.isEmpty(reqBody.getPicture())){
//                fileMsg = filePost(reqBody.getPicture());
//                if(200!=fileMsg.getCode()){
//                    result.setMsg("上传图片文件文件失败");
//                    result.setCode(-101);
//                    return result;
//                }
//                reqBody.setPicture(propAccessorUtils.getProperties("sms_file")+"/"+fileMsg.getMsg());
//            }

            Boolean bool = isLawful(reqBody,result);
            if(!bool){//判断彩信文件是否合法
                return result;
            }
            List<SmsPlatform> smsPlatformsVideo= SmsPlatformService.queryBySmsTypeAndIsEmploy(CommonConstants.SMS_VIDEO);
            smsVideoService.smsVideoSend(account,result,reqBody,smsPlatformsVideo,LT,YD,DX);
        }else {
            return  result.fill(JsonResponseMsg.CODE_FAIL,"发送的短信类型选择错误","");
        }
        return  result;
    }



    //判断彩信内容是否合法
    public static Boolean isLawful(ReqBody reqBody,JsonResponseMsg result) {
        //判断视频格式
        File tempFile =new File(reqBody.getVideo());
        String fileName = tempFile.getName();
        String gehsi =  fileName.substring(fileName.length()-3,fileName.length());
        if(!"3gp".equals(gehsi) && !"mp4".equals(gehsi)){
            result.setMsg("视频格式不对,请传入mp4，或者3gp格式的");
            result.setCode(1);
            result.setMsgId("");
            return false;
        }

        //计算彩信内容长度大小不能大于1.99M
        Long length = null;
        try {
            length = Utils.getFileSize(new File(reqBody.getVideo()));
            if(!StringUtils.isEmpty(reqBody.getPicture())){
                length +=Utils.getFileSize(new File(reqBody.getPicture()));
                String picType = Utils.getPicType(new FileInputStream(new File(reqBody.getPicture())));
                if(!CommonConstants.TYPE_GIF.equals(picType)&&!CommonConstants.TYPE_JPG.equals(picType)&&!CommonConstants.TYPE_PNG.equals(picType)){
                    result.setMsg("你的图片格式不对，请传入jpg,png,jif格式的");
                    result.setCode(1);
                    result.setMsgId("");
                    return false;
                }
            }
            if(CommonConstants.SMS_VIDEO_MAX_LENGTH < length){
                result.setMsg("你的短信内容太大了");
                result.setCode(1);
                result.setMsgId("");
                return false;
            }
        } catch (IOException e) {
            result.setMsg("获取文件大小长度错误");
            result.setCode(1);
            result.setMsgId("");
            e.printStackTrace();
            return false;
        }
        return  true;
    }


    /**
     * 查询信息状态
     */
    @ResponseBody
    @RequestMapping(value = "query" ,method = RequestMethod.GET)
    public JsonResponseMsg query(String msId){
        JsonResponseMsg result = new JsonResponseMsg();
        if(StringUtils.isEmpty(msId)){
            return result.fill(JsonResponseMsg.CODE_FAIL,"查询失败","");
        }
        SmsSendDetails smsSendDetails = smsSendDetailsMapper.queryBymsId(msId);
        Map<String,Object> map = new HashMap<>();
        map.put("isSend",smsSendDetails.getIsSend());
        map.put("assessor",smsSendDetails.getAssessor());
        return result.fill(JsonResponseMsg.CODE_SUCCESS,"查询成功",map);
    }

}
