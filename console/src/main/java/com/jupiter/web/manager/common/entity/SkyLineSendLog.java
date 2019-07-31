package com.jupiter.web.manager.common.entity;

import com.tron.common.mysql.mybatis.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Table;

@Data
@Table(name = "jpt_skyline_send_log")
public class SkyLineSendLog extends BaseEntity<String> {
    private String numbers;
    private String content;
    private String status;
    private Integer success;
    private Integer fail;
    private Long beginTime;
    private Long endTime;
    private String sendIds;
    private String addUser;
    private String updateUser;
}
