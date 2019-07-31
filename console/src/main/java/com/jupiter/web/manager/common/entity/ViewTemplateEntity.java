package com.jupiter.web.manager.common.entity;

import com.tron.common.mysql.mybatis.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Table;

/**
 * Created by zhangxiqiang on 2019/7/29.
 */
@Data
@Table(name = "jpt_view_template")
public class ViewTemplateEntity  extends BaseEntity<String> {
    private String path;
    private String code;
    private Integer actice;
}
