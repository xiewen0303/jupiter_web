package com.jupiter.web.manager.bus.article.dto.request;

import lombok.Data;

/**
 * Created by zhangxiqiang on 2019/7/23.
 */
@Data
public class ArticleInfoRequest {
    private String article_id; // 文章id，新建的文章没有这个字段
    private String category_id;
    private String head_pic;
    private String title;
    private String content; // html
    private Integer type; // 1:保存 ， 2：发布
}
