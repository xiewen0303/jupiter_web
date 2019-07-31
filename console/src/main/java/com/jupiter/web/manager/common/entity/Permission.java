package com.jupiter.web.manager.common.entity;

import com.tron.common.mysql.mybatis.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Table(name = "permission")
public class Permission extends BaseEntity<Long> {
    //权限描述
    private String description;
    //权限名称
    private String permissionName;
    //权限排序
    private Long priority;
    //父级id
    private Long parentId;
    //权限url
    private String url;
}
