package com.cunyun.smsforeign.dal.sevice.impl;

import com.cunyun.smsforeign.common.JsonResponseMsg;
import com.cunyun.smsforeign.common.SmsPlatformCode;
import com.cunyun.smsforeign.common.Utils;
import com.cunyun.smsforeign.common.WeightRandom;
import com.cunyun.smsforeign.dal.ReqBody;
import com.cunyun.smsforeign.dal.model.Account;
import com.cunyun.smsforeign.dal.model.SmsPlatform;
import com.cunyun.smsforeign.dal.sevice.SmsCharactersService;
import com.cunyun.smsforeign.dal.sevice.SunJianServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 普通短信service实现类
 */
@Service
@Slf4j
public class SmsCharactersServiceImpl implements SmsCharactersService {
    @Resource
    private SunJianServer sunJianServer;


    @Override
    public void smsCharacters(Account account,JsonResponseMsg result, ReqBody reqBody, List<SmsPlatform> smsPlatformsCharacters) {

//        Map<String,Integer> serverWeigthMap = new HashMap<>();//三网通的
//        Map<String,Integer> serverWeigthMapLD = new HashMap<>();//支持联通的
//        Map<String,Integer> serverWeigthMapDX = new HashMap<>();//支持电信的
//        Map<String,Integer> serverWeigthMapYD = new HashMap<>();//支持移动的
//        Utils.route(serverWeigthMap,serverWeigthMapLD,serverWeigthMapDX,serverWeigthMapYD,smsPlatformsCharacters);
//        if(StringUtils.isEmpty(serverWeigthMap)){
//            log.error("没有短信运营商可以提供error");
//            result.setCode(1);
//            result.setMsg("没有短信运营商可以提供");
//            result.setMsgId("");
//            return;
//        }
//        String smsPlatformcode = WeightRandom.weightRandom(serverWeigthMap);//路由出三网通的商家
//        String smsPlatformcodeLD = WeightRandom.weightRandom(serverWeigthMapLD);//路由出联通的商家
//        String smsPlatformcodeYD = WeightRandom.weightRandom(serverWeigthMapYD);//路由出移动的商家
//        String smsPlatformcodeDX = WeightRandom.weightRandom(serverWeigthMapDX);//路由出电信的商家
//
//        //三网通商家
//        if(SmsPlatformCode.SUN_JIAN_CODE.equals(smsPlatformcode)){//笋尖普通短信平台普通短信三网通(后面如果有别家支持的话需要做接口相应失败处理)
//
//        }

        sunJianServer.smsCharactersSend(account,result,reqBody);
        //联通商家


        //电信商家

        //移动商家

    }
}
