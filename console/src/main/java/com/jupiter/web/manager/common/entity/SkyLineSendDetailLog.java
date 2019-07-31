package com.jupiter.web.manager.common.entity;

import com.tron.common.mysql.mybatis.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Table;

@Data
@Table(name = "jpt_skyline_send_detail_log")
public class SkyLineSendDetailLog extends BaseEntity<String> {
    private String number;
    private Long sendTime;
    private String status;
    private String sendId;
    private String addUser;
    private String updateUser;
}
