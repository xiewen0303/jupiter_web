package com.jupiter.web.manager.common.entity;


import com.tron.common.mysql.mybatis.entity.BaseEntity;
import lombok.Data;
import javax.persistence.Table;

@Data
@Table(name = "jpt_system_cfg")
public class ConfigEntity extends BaseEntity<String> {

    private String  code;
    private String value;
    private String remark;
    private Long addUser;
    private Long updateUser;

}
