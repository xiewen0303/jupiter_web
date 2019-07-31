package com.jupiter.web.manager.bus.articleReview.dto;

import com.jupiter.web.manager.common.base.PageOutputDto;
import lombok.Data;

import java.util.List;

/**
 * @author fuyuling
 * @created 2019/7/23 18:24
 */
@Data
public class ArticleReviewQueryOutputDto {

    private String article_id; // 文章ID
    private Long timestamp; // 查询数据的时间戳，上拉加载下一页时由客户端传入，并只查询该时间以前的数据
    private List<ArticleReviewDto> new_reviews; // 最新2条评论
    private PageOutputDto<ArticleReviewDto> reviews; // 其他评论

}