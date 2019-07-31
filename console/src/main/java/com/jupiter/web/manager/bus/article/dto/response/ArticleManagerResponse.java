package com.jupiter.web.manager.bus.article.dto.response;

import com.jupiter.web.manager.bus.articleReview.dto.ArticleReviewDto;
import com.jupiter.web.manager.common.entity.ArticleInfoEntity;
import lombok.Data;

import java.util.List;

@Data
public class ArticleManagerResponse   {

    private ArticleInfoEntity articleInfo;

    private Integer likeTotal; //点赞总数

    private Integer commentTotal; //评论总数

    private String categoryName;

    private  List<ArticleReviewDto> articleReviewDtos; //评论数据

}
