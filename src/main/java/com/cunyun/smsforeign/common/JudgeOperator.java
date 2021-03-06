package com.cunyun.smsforeign.common;


import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * Created by 15600 on 2017/8/7.
 */
public class JudgeOperator {
    /*
    1、移动号段有134,135,136,137, 138,139,147,150,151, 152,157,158,159,178,182,183,184,187,188。
    2、联通号段有130，131，132，155，156，185，186，145，176。
    3、电信号段有133，153，177.173，180，181，189。
    */
    static String YD = "^[1]{1}(([3]{1}[4-9]{1})|([5]{1}[012789]{1})|([8]{1}[23478]{1})|([4]{1}[7]{1})|([7]{1}[8]{1}))[0-9]{8}$";
    static String LT = "^[1]{1}(([3]{1}[0-2]{1})|([5]{1}[56]{1})|([8]{1}[56]{1})|([4]{1}[5]{1})|([7]{1}[6]{1}))[0-9]{8}$";
    static String DX = "^[1]{1}(([3]{1}[3]{1})|([5]{1}[3]{1})|([8]{1}[09]{1})|([7]{1}[37]{1}))[0-9]{8}$";

    public static String matchNum(String mobPhnNum) {
        /**
         * 入参：手机号
         *return param
         * code (int):200 手机号符合要求  code:500手机号不合要求
         * type  (int): 1:YD 2:LT 3:DX
         */
//        JSONObject jsonObject=new JSONObject();
        // 判断手机号码是否是11位
        if (mobPhnNum.length() == 11) {
            // 判断手机号码是否符合中国移动的号码规则
            if (mobPhnNum.matches(YD)) {
               return  CommonConstants.YD;
            }
            // 判断手机号码是否符合中国联通的号码规则
            else if (mobPhnNum.matches(LT)) {
                return  CommonConstants.LT;
            }
            // 判断手机号码是否符合中国电信的号码规则
            else if (mobPhnNum.matches(DX)) {
                return  CommonConstants.DX;
            }

        }
        return CommonConstants.NO_MOBILE;
    }
    public static void main(String[] args) {

        String tel="13067990008,13067712129,15921138111,18767162311,15757127811,18658151212,15858179047,18605715080,13735490975,13957143710,13501897088,15058163331,18667163586,18868698080,13758139221,18767983108,18767164159,18767192025";
        String YD="";
        String LT="";
        String arrs[] = tel.split(",");
        for (String arr:arrs) {
            if(matchNum(arr).equals(CommonConstants.YD)){
                if(StringUtils.isEmpty(YD)){
                    YD +=arr;
                }else{
                    YD +=","+arr;
                }

            }
            if(matchNum(arr).equals(CommonConstants.LT)){
                if(StringUtils.isEmpty(YD)){
                    LT +=arr;
                }else{
                    LT +=","+arr;
                }

            }
        }
        System.out.println("YD:"+YD);
        System.out.println("LT:"+LT);
    }
}
