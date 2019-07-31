package com.jupiter.web.manager.common.entity;

import com.tron.common.mysql.mybatis.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Table;

@Data
@Table(name = "role_permission")
public class RolePermission extends BaseEntity<Long> {
    //权限id
    private Long permissionId;
    //角色id
    private Long roleId;
}
