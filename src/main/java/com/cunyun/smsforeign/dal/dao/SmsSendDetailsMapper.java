package com.cunyun.smsforeign.dal.dao;

import com.cunyun.smsforeign.dal.model.SmsSendDetails;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SmsSendDetailsMapper {
    int insert(SmsSendDetails record);

    int insertSelective(SmsSendDetails record);

    SmsSendDetails selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SmsSendDetails record);

    int updateByPrimaryKey(SmsSendDetails record);

    int insertByBatch(List<SmsSendDetails> list);

    List<String> queryNoSendSmsCharacteristic(@Param("code") String code);

    List<SmsSendDetails> queryByCharacteristic(@Param("characteristic") String characteristic,@Param("code")String code);

    void updatebByCharacteristicAndCode(@Param("characteristic")String characteristic, @Param("code")String muChenCode);

    SmsSendDetails queryBymsId(@Param("msId")String msId);

    SmsSendDetails queryBycharacteristicAndCode(@Param("characteristic")String characteristic, @Param("code")String sunJianCode);

}