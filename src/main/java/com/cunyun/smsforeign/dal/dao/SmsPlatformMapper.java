package com.cunyun.smsforeign.dal.dao;

import com.cunyun.smsforeign.dal.model.SmsPlatform;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SmsPlatformMapper {
    int insert(SmsPlatform record);

    int insertSelective(SmsPlatform record);

    SmsPlatform selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SmsPlatform record);

    int updateByPrimaryKey(SmsPlatform record);

    List<SmsPlatform> queryBySmsTypeAndIsEmploy(int smsType);

    SmsPlatform queryByCode(@Param("supplierCode") String supplierCode,@Param("type") Integer type);
}