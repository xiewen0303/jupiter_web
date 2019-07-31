package com.jupiter.web.manager.common.entity;

import com.tron.common.mysql.mybatis.entity.BaseEntity;
import com.jupiter.web.manager.utils.DateUtil;
import lombok.Data;

import javax.persistence.Table;

@Data
@Table(name = "jpt_banner")
public class SouceSiteEntity extends BaseEntity<Long> {

    private String code;
    private String name;
    private Integer enabled;
}
