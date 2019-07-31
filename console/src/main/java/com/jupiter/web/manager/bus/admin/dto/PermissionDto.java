package com.jupiter.web.manager.bus.admin.dto;

import lombok.Data;

@Data
public class PermissionDto extends BaseDto {

    //权限ID
    private Long id;

    //权限名
    private String permissionName;
}
