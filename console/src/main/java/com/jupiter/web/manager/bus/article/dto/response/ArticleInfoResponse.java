package com.jupiter.web.manager.bus.article.dto.response;

import lombok.Data;

import java.util.List;

/**
 * Created by zhangxiqiang on 2019/7/23.
 */
@Data
public class ArticleInfoResponse {
    private String article_id; // 文章id，新建的文章没有这个字段
    private String head_pic;
    private String title;
    private String content; // html
    private String category_id;
    private String category_name;
    private ReviewInfo review_info; // 部分评论信息
    private Long publish_time; // 发布时间
    private String name; // 发布用户


    @Data
    public static class ReviewInfo{
        private List<ReviewDetail> detail;
        private Integer review_count;
    }

    @Data
    public static class ReviewDetail{
        private String id;
        private Long like_num;
        private String from_username;
        private String avatar_file_url;
        private String content;
        private Long review_time;
        private Integer is_like; // 当前用户是否点赞，1点赞，0未点赞
    }
}
