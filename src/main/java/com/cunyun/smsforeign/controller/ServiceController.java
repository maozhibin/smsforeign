package com.cunyun.smsforeign.controller;

import com.cunyun.smsforeign.common.ConfigurablePropAccessorUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.awt.*;
import java.io.File;
import java.io.IOException;

@CrossOrigin
@RestController
@RequestMapping("sms")
public class ServiceController {
    @Resource
    ConfigurablePropAccessorUtils propAccessorUtils;

    @RequestMapping(value = "service/check" ,method = RequestMethod.GET)
    public Long check(){
        return System.currentTimeMillis();
    }
}
