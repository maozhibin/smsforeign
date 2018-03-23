package com.cunyun.smsforeign.controller;


import com.cunyun.smsforeign.common.*;
import com.cunyun.smsforeign.dal.ReqBody;
import com.cunyun.smsforeign.dal.dao.SmsSendDetailsMapper;
import com.cunyun.smsforeign.dal.model.Account;
import com.cunyun.smsforeign.dal.model.SmsPlatform;
import com.cunyun.smsforeign.dal.model.SmsSendDetails;
import com.cunyun.smsforeign.dal.sevice.*;
import com.cunyun.smsforeign.task.TaskModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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
    @Resource
    private SunJianServer sunJianServer;
    @Resource
    private MuChenService muChenService;

    /**
     * 模板申请
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "applay" ,method = RequestMethod.POST)
    public JsonResponseMsg applay(HttpServletRequest request) {
        JsonResponseMsg result = new JsonResponseMsg();
        ReqBody reqBody = new ReqBody();
        if("2".equals(request.getParameter("type"))){
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            MultipartFile video = multipartRequest.getFile("video");
            if(video==null){
                return result.fill(JsonResponseMsg.CODE_FAIL,"请传入视频","");
            }
            reqBody.setVideo(fileUpload(propAccessorUtils.getProperties("sms_file"),video));
            MultipartFile picFile = multipartRequest.getFile("picFile");
            if(picFile!=null){
                reqBody.setPicture(fileUpload(propAccessorUtils.getProperties("sms_file"),picFile));
            }
        }
        reqBody.setContent(request.getParameter("content"));
        reqBody.setType(request.getParameter("type"));
        reqBody.setUserName(request.getParameter("userName"));
//        reqBody.setMobile(request.getParameter("mobile"));
        reqBody.setPassword(request.getParameter("password"));
        reqBody.setTitle(request.getParameter("title"));
        reqBody.setSign(request.getParameter("sign"));
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
//        if(StringUtils.isEmpty(reqBody.getMobile())){
//            return  result.fill(JsonResponseMsg.CODE_FAIL,"请输入手机号","");
//        }
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
        //手机号码判断归属运营商
//        String mobile = reqBody.getMobile();
//        String[] mobiles = mobile.split(",");
//        List<String> LT = new ArrayList<>();
//        List<String> YD = new ArrayList<>();
//        List<String> DX = new ArrayList<>();
//        for (String s:mobiles) {
//            String mobileType = JudgeOperator.matchNum(s);
//            if(mobileType.equals(CommonConstants.DX)){
//                DX.add(s);
//            }else if(mobileType.equals(CommonConstants.LT)){
//                LT.add(s);
//            }else if(mobileType.equals(CommonConstants.YD)){
//                YD.add(s);
//            }
//        }
        if(reqBody.getType().equals("1")){ //普通短信
            if(StringUtils.isEmpty(reqBody.getContent())){
                return  result.fill(JsonResponseMsg.CODE_FAIL,"请输入你要发送的内容","");
            }
            List<SmsPlatform> smsPlatformsCharacters = SmsPlatformService.queryBySmsTypeAndIsEmploy(CommonConstants.SMS_COMMON);
            SmsCharactersService.smsCharacters(account,result,reqBody,smsPlatformsCharacters);
        }else if(reqBody.getType().equals("2")){//视频短信
            if(StringUtils.isEmpty(reqBody.getVideo())){
                return  result.fill(JsonResponseMsg.CODE_FAIL,"请传入你要发送的视频","");
            }
            Boolean bool = isLawful(reqBody,result);
            if(!bool){//判断彩信文件是否合法
                return result;
            }
            List<SmsPlatform> smsPlatformsVideo= SmsPlatformService.queryBySmsTypeAndIsEmploy(CommonConstants.SMS_VIDEO);
            smsVideoService.smsVideoSend(account,result,reqBody,smsPlatformsVideo);
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
     * 短信下发
     * mobiles格式 13093783517,13093793518
     * Characteristic格式 VSMS00003064，360041
     */
    @ResponseBody
    @RequestMapping(value = "send" ,method = RequestMethod.POST)
    public JsonResponseMsg send(String Characteristic,String mobiles,String userName,String pwd) {
        JsonResponseMsg result = new JsonResponseMsg();
        if(StringUtils.isEmpty(Characteristic)){
            return  result.fill(JsonResponseMsg.CODE_FAIL,"请传入模板code","");
        }
        if(StringUtils.isEmpty(mobiles)){
            return  result.fill(JsonResponseMsg.CODE_FAIL,"请传入手机号","");
        }
        //判断账号是否合法
        Account account = accountSerivce.queryByUserName(userName);
        if(account==null || !account.getPwd().equals(pwd)){
            return  result.fill(JsonResponseMsg.CODE_FAIL,"账号或者密码错误","");
        }
        //手机号码判断归属运营商,目前只支持；联通和移动的
        String[] mobileArr = mobiles.split(",");
        String LT = "";
        String YD = "";
        String DX = "";
        for (String s:mobileArr) {
            String mobileType = JudgeOperator.matchNum(s);
            if(mobileType.equals(CommonConstants.YD)){
                if(StringUtils.isEmpty(YD)){
                    YD +=s;
                }else{
                    YD +=","+s;
                }
            }
            if(mobileType.equals(CommonConstants.LT)){
                if(StringUtils.isEmpty(LT)){
                    LT +=s;
                }else{
                    LT +=","+s;
                }
            }
//            if(mobileType.equals(CommonConstants.DX)){
//                if(StringUtils.isEmpty(DX)){
//                    DX +=s;
//                }else{
//                    DX +=","+s;
//                }
//            }
        }
        String CharacteristicArr[] = Characteristic.split(",");
        for (String characteristic:CharacteristicArr) {
            SmsSendDetails smsSendDetails =  smsSendDetailsMapper.queryPlatformCodeByCharacteristic(characteristic);
            if(smsSendDetails==null){
                return result.fill(JsonResponseMsg.CODE_FAIL,"不存在改模板","");
            }
           if(SmsPlatformCode.SUN_JIAN_CODE.equals(smsSendDetails.getSupplierCode())){//笋尖的模板
               if(!StringUtils.isEmpty(YD)){
                   //笋尖短信接口
                    String loginname = propAccessorUtils.getProperties("sunjian_loginname");
                    String password = propAccessorUtils.getProperties("sunjian_pwd");
                    String sendurl = propAccessorUtils.getProperties("c_sunjian_send_video_templet_url");
                    //普通短信接口相关数据
                    String loginnameCharacters = propAccessorUtils.getProperties("sunjian_characters_loginname");
                    String passwordCharacters = propAccessorUtils.getProperties("sunjian_characters_pwd");
                    String send = "";

                        if(smsSendDetails.getExtVideoId()==1){//文字短信下发
                            send=sunJianServer.send(sendurl,loginnameCharacters,passwordCharacters,characteristic,YD,smsSendDetails.getId());
                        }else if(smsSendDetails.getExtVideoId()==2){//视频短信下发
                            send=sunJianServer.send(sendurl,loginname,password,characteristic,YD,smsSendDetails.getId());
                        }
                        if(!"0".equals(send)){
                            log.error("笋尖下发短信接口失败");
                            return result.fill(JsonResponseMsg.CODE_FAIL,"下发成功失败","");
                        }
                }

           }else if(SmsPlatformCode.MU_CHEN_CODE.equals(smsSendDetails.getSupplierCode())){//牧尘的模板,目前只可以发送视频
               if(!StringUtils.isEmpty(LT)){
                   String key = propAccessorUtils.getProperties("mucheng_key");
                   String username = propAccessorUtils.getProperties("mucheng_username");
                   String sendUrl = propAccessorUtils.getProperties("mucheng_sen");
                   TaskModel taskModel = new TaskModel();
                   taskModel.setCharacteristic(characteristic);
                   taskModel.setMobile(LT);
                   muChenService.send(key,sendUrl,username,taskModel,smsSendDetails.getId());
                   if(!"0".equals(taskModel.getCode())){
                       log.error("牧尘下发短信接口失败");
                       return result.fill(JsonResponseMsg.CODE_FAIL,"下发成功失败","");
                   }
               }
           }
        }
        return result.fill(JsonResponseMsg.CODE_SUCCESS,"下发成功","");
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
        if(smsSendDetails!=null){
            map.put("isSend",smsSendDetails.getIsSend());
            map.put("assessor",smsSendDetails.getAssessor());
        }else {
            return result.fill(JsonResponseMsg.CODE_SUCCESS,"未查询到改短信内容","");
        }
        return result.fill(JsonResponseMsg.CODE_SUCCESS,"查询成功",map);
    }
}
