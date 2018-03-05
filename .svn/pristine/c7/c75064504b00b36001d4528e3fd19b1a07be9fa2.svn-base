package com.cunyun.smsforeign.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

@Component
public class ConfigurablePropAccessorUtils {
    @Autowired
    private Environment env;

    public String getProperties(String key) {
        return env.getProperty(key);
    }



}
