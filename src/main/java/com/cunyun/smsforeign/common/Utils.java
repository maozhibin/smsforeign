package com.cunyun.smsforeign.common;

import com.cunyun.smsforeign.dal.model.SmsPlatform;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;

import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

public class Utils {
    public static final String TYPE_JPG = "jpg";
    public static final String TYPE_GIF = "gif";
    public static final String TYPE_PNG = "png";
    /**
     * 获得文件的大小
     * @param file
     * @return
     * @throws IOException
     */
    public static long getFileSize(File file) throws IOException {
        FileInputStream fis = new FileInputStream(file);

        FileChannel fc = fis.getChannel();
        long length = fc.size();
        fis.close();
        return length;
    }

    public static String bytesToHexString(byte[] src){
        StringBuilder stringBuilder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    /**
     * 根据文件流判断图片类型
     * @param fis
     * @return jpg/png/gif/bmp
     */
    public static String getPicType(FileInputStream fis) {
        //读取文件的前几个字节来判断图片格式
        byte[] b = new byte[4];
        try {
            fis.read(b, 0, b.length);
            String type = bytesToHexString(b).toUpperCase();
            if (type.contains("FFD8FF")) {
                return TYPE_JPG;
            } else if (type.contains("89504E47")) {
                return TYPE_PNG;
            } else if (type.contains("47494638")) {
                return TYPE_GIF;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            if(fis != null){
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
    /**
     * 将不同的商家路由到不通数组中
     */
    public static void route(Map<String,Integer> serverWeigthMap, Map<String,Integer> serverWeigthMapLD, Map<String,Integer> serverWeigthMapDX, Map<String,Integer> serverWeigthMapYD, List<SmsPlatform> smsPlatform){
//        Map<String,Integer> serverWeigthMap = new HashMap<>();//三网通的
//        Map<String,Integer> serverWeigthMapLD = new HashMap<>();//支持联通的
//        Map<String,Integer> serverWeigthMapDX = new HashMap<>();//支持电信的
//        Map<String,Integer> serverWeigthMapYD = new HashMap<>();//支持移动的
        for (SmsPlatform smsPlatformsCharacter:smsPlatform) {
            if(CommonConstants.DX_VALUE.equals(smsPlatformsCharacter.getSupportMobile())){
                serverWeigthMapDX.put(smsPlatformsCharacter.getSupplierCode(), smsPlatformsCharacter.getWight());
            }else if(CommonConstants.LT_VALUE.equals(smsPlatformsCharacter.getSupportMobile())){
                serverWeigthMapLD.put(smsPlatformsCharacter.getSupplierCode(), smsPlatformsCharacter.getWight());
            }else if(CommonConstants.YD_VALUE.equals(smsPlatformsCharacter.getSupportMobile())){
                serverWeigthMapYD.put(smsPlatformsCharacter.getSupplierCode(), smsPlatformsCharacter.getWight());
            }else if(CommonConstants.SAN_WANG_TONG_VALUE.equals(smsPlatformsCharacter.getSupportMobile())){
                serverWeigthMap.put(smsPlatformsCharacter.getSupplierCode(), smsPlatformsCharacter.getWight());
            }

        }
    }



    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
//    public static void main(String[] args) throws IOException {
//        HttpClient httpclient = new DefaultHttpClient();
//        HttpPost httppost = new HttpPost("http://127.0.0.1:55670/sms/fileUpload");
//        MultipartEntity reqEntity = new MultipartEntity();
//        FileBody videoFile = new FileBody(new File("E:\\\\video\\\\test111.png"));
//        reqEntity.addPart("video", videoFile);
//        httppost.setEntity(reqEntity);
//        //4：执行httppost对象，从而获得信息
//        HttpResponse response = httpclient.execute(httppost);
////        saveFile(reqEntity.getContent());
//    }



}
