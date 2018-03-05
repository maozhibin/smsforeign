package com.cunyun.smsforeign.dal.sevice.impl;

import com.cunyun.smsforeign.dal.dao.SmsPlatformMapper;
import com.cunyun.smsforeign.dal.model.SmsPlatform;
import com.cunyun.smsforeign.dal.sevice.SmsPlatformService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Service
public class SmsPlatformServiceImpl implements SmsPlatformService {
    @Resource
    private SmsPlatformMapper smsPlatformMapper;

    @Override
    public List<SmsPlatform> queryBySmsTypeAndIsEmploy(int smsType) {
        return smsPlatformMapper.queryBySmsTypeAndIsEmploy(smsType);
    }
}
