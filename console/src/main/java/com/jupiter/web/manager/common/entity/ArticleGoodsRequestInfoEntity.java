package com.jupiter.web.manager.common.entity;

import com.tron.common.mysql.mybatis.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Table;

/**
 * Created by zhangxiqiang on 2019/7/23.
 */
@Data
@Table(name = "jpt_article_goods_request_info")
public class ArticleGoodsRequestInfoEntity extends BaseEntity<String> {

    private String billNo;
    private String url;
    private String articleGoodsInfoId;
    private Integer status;
    private String addUser;
    private String updateUser;
    private Long addTime;
    private Long updateTime;

}
