package com.jupiter.web.manager.bus.admin.dto;

import lombok.Data;

import java.util.List;

/**
 * Created by liuwei
 * date 2017-04-13
 */
@Data
public class RolePermissionDto {

    private Long roleId;
    private List<Long> permissionList;
}
