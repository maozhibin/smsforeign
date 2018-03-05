package com.cunyun.smsforeign.dal.dao;

import com.cunyun.smsforeign.dal.model.RolePermission;

public interface RolePermissionMapper {
    int insert(RolePermission record);

    int insertSelective(RolePermission record);
}