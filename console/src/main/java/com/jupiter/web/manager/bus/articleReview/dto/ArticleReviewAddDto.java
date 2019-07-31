package com.jupiter.web.manager.bus.articleReview.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * @author fuyuling
 * @created 2019/7/23 18:17
 */
@Data
public class ArticleReviewAddDto {

    @NotBlank(message = "{article.review.content.blank}")
    @Length(message = "{article.review.length}", min= 5, max = 500)
    private String content;
    @NotBlank(message = "{params_error}")
    private String article_id;

}
