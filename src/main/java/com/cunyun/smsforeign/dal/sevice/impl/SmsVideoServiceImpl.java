package com.cunyun.smsforeign.dal.sevice.impl;

import com.cunyun.smsforeign.common.*;
import com.cunyun.smsforeign.dal.ReqBody;
import com.cunyun.smsforeign.dal.model.Account;
import com.cunyun.smsforeign.dal.model.SmsPlatform;
import com.cunyun.smsforeign.dal.sevice.MuChenService;
import com.cunyun.smsforeign.dal.sevice.SmsVideoService;
import com.cunyun.smsforeign.dal.sevice.SunJianServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 视频短信接口实现
 */
@Service
@Slf4j
public class SmsVideoServiceImpl implements SmsVideoService {
    @Resource
    private SunJianServer sunJianServer;
    @Resource
    private MuChenService muChenService;

    @Override
    public void smsVideoSend(Account account, JsonResponseMsg result, ReqBody reqBody, List<SmsPlatform> smsPlatformsVideo) {

//        Map<String,Integer> serverWeigthMap = new HashMap<>();//三网通的
//        Map<String,Integer> serverWeigthMapLD = new HashMap<>();//支持联通的
//        Map<String,Integer> serverWeigthMapDX = new HashMap<>();//支持电信的
//        Map<String,Integer> serverWeigthMapYD = new HashMap<>();//支持移动的
//        Utils.route(serverWeigthMap,serverWeigthMapLD,serverWeigthMapDX,serverWeigthMapYD,smsPlatformsVideo);
//        if(StringUtils.isEmpty(serverWeigthMap)){
//            log.error("没有短信运营商可以提供error");
//            result.setCode(1);
//            result.setMsg("没有短信运营商可以提供");
//            return;
//        }
//        String smsPlatformcode = WeightRandom.weightRandom(serverWeigthMap);//路由出三网通的商家
//        String smsPlatformcodeLD = WeightRandom.weightRandom(serverWeigthMapLD);//路由出联通的商家
//        String smsPlatformcodeYD = WeightRandom.weightRandom(serverWeigthMapYD);//路由出移动的商家
//        String smsPlatformcodeDX = WeightRandom.weightRandom(serverWeigthMapDX);//路由出电信的商家
//
//
//        //防止信息被修改，所以每个运营商一开始就建立自己需要处理的信息
//        ReqBody reqBodyLT = reqBody;
//        ReqBody reqBodyDX = reqBody;
//        ReqBody reqBodyYD = reqBody;
//        ReqBody reqBodSAN = reqBody;
//
//        //联通商家(后面如果多家接入的话需要做接口相应失败处理)
//        if(LT.size()>0){
//            if(SmsPlatformCode.MU_CHEN_CODE.equals(smsPlatformcodeLD)){//牧尘视频短信平台
//                String mobile =mobile(LT);
//                reqBodyLT.setMobile(mobile);
                muChenService.smsVideoApply(account,result,reqBody);
//            }
//        }
        //电信商家

//        //移动商家(后面如果多家接入的话需要做接口相应失败处理)
//        if(YD.size()>0){
//            if(SmsPlatformCode.SUN_JIAN_CODE.equals(smsPlatformcodeYD)){//笋尖视频短信平台
//                String mobile =mobile(YD);
//                reqBodyYD.setMobile(mobile);
                sunJianServer.smsVideoApply(account,result,reqBody);
//            }
//        }

        //三网通商家
    }


    //手机号处理
    public String mobile(List<String> mobiles){
        String mobile ="";
        for(int i=0;i<mobiles.size();i++){
            if(i==mobiles.size()-1){
                mobile +=mobiles.get(i);
            }else{
                mobile +=mobiles.get(i)+",";
            }
        }
        return  mobile;
    }


    /**
     * 移动运营商调取(接口相应失败处理)
     */
//    public void YDpalatForm( Map<String,Integer> serverWeigthMapYD,ReqBody reqBody,JsonResponseMsg result,Account account,List<String> YD){
////      serverWeigthMapYD.remove();
//        String smsPlatformcodeYD = WeightRandom.weightRandom(serverWeigthMapYD);//路由出移动的商家
//        if(serverWeigthMapYD.equals("")){
//            return;
//        }
//        YDform(smsPlatformcodeYD,YD,reqBody,result,account);
//        if(result.getCode()!=200){//如果商家调取失败则调取别家的接口接着转发
//            serverWeigthMapYD.remove(smsPlatformcodeYD);
//            YDpalatForm(serverWeigthMapYD,reqBody,result,account,YD);
//        }
//
//    }
//    public void YDform(String smsPlatformcodeYD,List<String> YD,ReqBody reqBody,JsonResponseMsg result,Account account){
//        if(SmsPlatformCode.SUN_JIAN_CODE.equals(smsPlatformcodeYD)){//笋尖视频短信平台
//            String mobile ="";
//            for(int i=0;i<YD.size();i++){
//                if(i==YD.size()-1){
//                    mobile +=YD.get(i);
//                }else{
//                    mobile +=YD.get(i)+",";
//                }
//            }
//            reqBody.setMobile(mobile);
//            sunJianServer.smsVideoApply(account,result,reqBody);
//        }
//    }


}
