package com.jupiter.web.manager.common.entity;

import com.tron.common.mysql.mybatis.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Table;

@Data
@Table(name = "jpt_sms_captcha")
public class Captcha extends BaseEntity<String> {
    private String mobile;
    private String type;
    private String code;
    private Integer validTime;
    private String clientIp;
    private String addUser;
    private String updateUser;
}
