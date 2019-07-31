package com.jupiter.web.manager.common.entity;

import com.tron.common.mysql.mybatis.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Table;

/**
 * @author fuyuling
 * @created 2019/7/23 18:43
 */
@Data
@Table(name = "jpt_article_review")
public class ArticleReviewEntity extends BaseEntity<String> {

    private String articleId;
    private String reviewId;
    private String content;
    private String fromUid;
    private String toUid;
    private String fromUsername;
    private String toUsername;
    private Long reviewTime;
    private Integer checkStatus;
    private String checkAdmin;
    private String addUser;
    private String updateUser;
    private Boolean deleteFlag;

}
