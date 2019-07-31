package com.jupiter.web.manager.common.entity;

import com.jupiter.web.manager.utils.DateUtil;
import com.tron.common.mysql.mybatis.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Table;

@Data
@Table(name = "jpt_article_audit")
public class ArticleAuditEntity extends BaseEntity<String> {

    private String registMsg;
    private String articleId;
    private Integer status;

    private String addUser;
    private String updateUser;

    public String getCreateTimeFormate(){
        return DateUtil.formatDate(this.addTime,"yyyy-MM-dd  HH:mm:ss");
    }

    public String getLastModifyTimeFormate(){
        return DateUtil.formatDate(this.updateTime,"yyyy-MM-dd  HH:mm:ss");
    }
}