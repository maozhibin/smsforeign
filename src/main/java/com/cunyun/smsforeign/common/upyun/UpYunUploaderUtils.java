package com.cunyun.smsforeign.common.upyun;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import main.java.com.UpYun;
import main.java.com.UpYun.PARAMS;
import main.java.com.upyun.Result;
import org.apache.commons.lang3.StringUtils;
import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.*;

public class UpYunUploaderUtils {
    // 运行前先设置好以下三个参数
//    private static final String BUCKET_NAME = "sms-foreign";
//    private static final String OPERATOR_NAME = "maozhibin";
//    private static final String OPERATOR_PWD = "maozhi2764147";

    public static final String BUCKET_NAME = "zone-admin";
    private static final String OPERATOR_NAME = "yeanping";
    private static final String OPERATOR_PWD = "liaoting5200";

    //上传测试文件
//    private static final String SAMPLE_PIC_FILE = "E:\\video\\1111.mp4";


//    private static File file = new File(SAMPLE_PIC_FILE);
    //保存路径 必须设置该参数
    private static String savePath = "/uploads/{year}{mon}{day}/{random32}{.suffix}";

    public static void main(String[] args) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
//        WriteFile();
    }

    /**
     * 文件上传
     */
    private static Result WriteFile(File file) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        final Map<String, Object> paramsMap = new HashMap<String, Object>();
        paramsMap.put(Params.SAVE_KEY, savePath);
        FormUploader uploader = new FormUploader(BUCKET_NAME, OPERATOR_NAME, OPERATOR_PWD);
        Result result = uploader.upload(paramsMap, file);
        return result;
//        System.out.println(result);
        //返回结果
//        System.out.println(uploader.upload(paramsMap, "1111".getBytes()));
    }





}
