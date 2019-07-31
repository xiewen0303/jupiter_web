package com.jupiter.web.manager.bus.article.dto.response;

import lombok.Data;

import javax.persistence.Transient;
import java.util.List;

/**
 * Created by zhangxiqiang on 2019/7/23.
 */
@Data
public class ArticleListResponse {
    private List<ArticleListDetail> detail;
    private Long timestamp;

    @Data
    public static class ArticleListDetail{
        private String article_id;
        private String icon; // 头像
        private String name; // 显示名
        private String title;
        private String head_pic;
        private Integer like_count;
        private Integer review_count;
        private Integer is_like; // 0:表示当前用户没有点赞，1表示当前用户已经点赞
        @Transient
        private Integer order;
    }
}
