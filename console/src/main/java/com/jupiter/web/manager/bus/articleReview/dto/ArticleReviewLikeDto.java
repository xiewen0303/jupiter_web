package com.jupiter.web.manager.bus.articleReview.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author fuyuling
 * @created 2019/7/23 18:24
 */
@Data
public class ArticleReviewLikeDto {

    @NotBlank(message = "{params_error}")
    private String target_id;
    @NotNull(message = "{params_error}")
    private Integer like;

}
