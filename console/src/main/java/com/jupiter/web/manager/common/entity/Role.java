package com.jupiter.web.manager.common.entity;

import com.tron.common.mysql.mybatis.entity.BaseEntity;
import lombok.Data;
import org.apache.openjpa.persistence.jdbc.Unique;

import javax.persistence.Table;

@Data
@Table(name = "role")
public class Role extends BaseEntity<Long> {
    //角色描述
    private String description;
    //角色名称
    @Unique
    private String roleName;
}
