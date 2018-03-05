package com.cunyun.smsforeign.controller;

import com.cunyun.smsforeign.common.ConfigurablePropAccessorUtils;
import com.cunyun.smsforeign.common.JsonResponseMsg;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

@CrossOrigin
@RequestMapping("sms")
@Controller
@Slf4j
public class receiveData {
    @Autowired
    private HttpServletRequest request;
    @Resource
    private ConfigurablePropAccessorUtils propAccessorUtils;

    @RequestMapping(value="/multipleSave", method= RequestMethod.POST )
    @ResponseBody
    public String multipleSave(@RequestParam("file") MultipartFile[] files){
        String fileName = null;
        String msg = "";
        if (files != null && files.length >0) {
            for(int i =0 ;i< files.length; i++){
                try {
                    fileName = files[i].getOriginalFilename();
                    byte[] bytes = files[i].getBytes();
                    BufferedOutputStream buffStream =
                            new BufferedOutputStream(new FileOutputStream(new File("/tmp/" + fileName)));
                    buffStream.write(bytes);
                    buffStream.close();
                    msg += "You have successfully uploaded " + fileName;
                } catch (Exception e) {
                    return "You failed to upload " + fileName + ": " + e.getMessage();
                }
            }
            return msg;
        } else {
            return "Unable to upload. File is empty.";
        }
    }

//    @RequestMapping("fileUpload")
//    @ResponseBody
//    public JsonResponseMsg fileUpload(@RequestParam("file") MultipartFile file) {
//        JsonResponseMsg jsonResponseMsg= new JsonResponseMsg();
//        // 判断文件是否为空
//        if (!file.isEmpty()) {
//            try {
//                // 文件保存路径
//                String filePath = propAccessorUtils.getProperties("sms_file") + "/"+file.getOriginalFilename();//服务器地址
////                String filePath = propAccessorUtils.getProperties("E:/upload") + "/"+file.getOriginalFilename();//本地
//                file.transferTo(new File(filePath));
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        // 重定向
//        return jsonResponseMsg.fill(JsonResponseMsg.CODE_SUCCESS,file.getOriginalFilename(),"");
//    }
}
