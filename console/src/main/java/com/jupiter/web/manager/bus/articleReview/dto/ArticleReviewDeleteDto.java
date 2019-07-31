package com.jupiter.web.manager.bus.articleReview.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author fuyuling
 * @created 2019/7/23 18:24
 */
@Data
public class ArticleReviewDeleteDto {

    @NotBlank(message = "{params_error}")
    private String review_id;

}
