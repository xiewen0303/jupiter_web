package com.jupiter.web.manager.common.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tron.common.mysql.mybatis.entity.BaseEntity;
import lombok.Data;
import org.apache.shiro.authz.SimpleAuthorizationInfo;

import javax.persistence.Table;
import javax.persistence.Transient;

@Data
@Table(name = "admin")
@JsonIgnoreProperties(value = {"handler"})
public class Admin extends BaseEntity<Long> {

    private Boolean forbid;
    //角色id
    private Long roleId;
    //登录名
    private String adminName;
    //密码
    private String password;
    //密码加密盐值
    private String salt;

    @Transient
    private String roleName;

    @Transient
    private SimpleAuthorizationInfo info;


}
