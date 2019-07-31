package com.jupiter.web.manager.bus.article.dto.request;

import lombok.Data;

/**
 * Created by zhangxiqiang on 2019/7/23.
 */
@Data
public class ArticleLikeRequest {
    private Integer like ; // 1: 点赞 ，0 ：取消点赞
    private String article_id;
}
