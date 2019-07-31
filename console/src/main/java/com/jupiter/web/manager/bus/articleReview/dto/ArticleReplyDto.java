package com.jupiter.web.manager.bus.articleReview.dto;

        import lombok.Data;

        import javax.persistence.Column;

/**
 * @author fuyuling
 * @created 2019/7/24 19:03
 */
@Data
public class ArticleReplyDto {

    private String id;
    private String content;
    private String from_username;
    private String to_username;
    private String from_uid;
    private String to_uid;
    private Long review_time;
    private Integer is_like; // 当前用户是否点赞，1点赞，0未点赞
    private Long like_num;

}
