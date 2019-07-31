package com.jupiter.web.manager.bus.articleReview.dto;

import com.jupiter.web.manager.common.base.PageInputDto;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author fuyuling
 * @created 2019/7/23 18:24
 */
@Data
public class ArticleReviewQueryInputDto extends PageInputDto {

    @NotBlank(message = "{params_error}")
    private String article_id;
    private Long timestamp;

}
