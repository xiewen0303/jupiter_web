package com.jupiter.web.manager.common.entity;

import com.tron.common.mysql.mybatis.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * Created by zhangxiqiang on 2019/7/23.
 */
@Data
@Table(name = "jpt_article_goods_info")
public class ArticleGoodsInfoEntity extends BaseEntity<String> {

    private String title;
    private BigDecimal price;
    private BigDecimal originPrice;
    private String origin;
    private String md5Url;
    private String url;
    private String icon;
    private String priceUnit;
    private BigDecimal discount;
    private BigDecimal score;
    private String addUser;
    private String updateUser;

}
