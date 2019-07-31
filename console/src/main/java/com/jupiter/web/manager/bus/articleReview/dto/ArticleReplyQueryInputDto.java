package com.jupiter.web.manager.bus.articleReview.dto;

import com.jupiter.web.manager.bus.admin.dto.BaseDto;
import com.jupiter.web.manager.common.base.PageInputDto;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author fuyuling
 * @created 2019/7/23 18:24
 */
@Data
public class ArticleReplyQueryInputDto extends PageInputDto {

    @NotBlank(message = "{params_error}")
    private String review_id;
    private Long timestamp;

}
