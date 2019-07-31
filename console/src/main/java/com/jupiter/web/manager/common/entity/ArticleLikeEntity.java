package com.jupiter.web.manager.common.entity;

import com.tron.common.mysql.mybatis.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * Created by zhangxiqiang on 2019/7/23.
 */
@Data
@Table(name = "jpt_article_like")
public class ArticleLikeEntity extends BaseEntity<String> {

    private String articleId; // 文章ID
    private String typeId; // 点赞目标ID
    private Integer type;
    @Column(name = "_like")
    private Integer like;
    private String uid;
    private Long operateTime;
    private String addUser;
    private String updateUser;

}
