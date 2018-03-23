package com.cunyun.smsforeign.common.muchen;

import sun.misc.BASE64Encoder;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;
import java.util.TreeMap;

public class MuChenUtil {

    /**
     * 生成秘钥
     *
     * @param tm
     * @param key
     * @return
     */
    public static String createSign(TreeMap<String, String> tm, String key) {
        StringBuffer buf = new StringBuffer(key);
        for (Map.Entry<String, String> en : tm.entrySet()) {
            String name = en.getKey();
            String value = en.getValue();
            if (!"sign".equals(name) && !"param".equals(name) && value != null && value.length() > 0 && !"null".equals(value)) {
                buf.append(name).append('=').append(value).append('&');
            }
        }
        String _buf = buf.toString();
        return _buf.substring(0, _buf.length() - 1);
    }


    /**
     * 将文件转成base64 字符串
     * @return *
     * @throws Exception
     */
    public static String encodeBase64File(String path) throws Exception {
        File file = new File(path);;
        FileInputStream inputFile = new FileInputStream(file);
        byte[] buffer = new byte[(int) file.length()];
        inputFile.read(buffer);
        inputFile.close();
        return new BASE64Encoder().encode(buffer);

    }



}
