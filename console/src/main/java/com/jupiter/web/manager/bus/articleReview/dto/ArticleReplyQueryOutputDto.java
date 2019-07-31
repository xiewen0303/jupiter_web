package com.jupiter.web.manager.bus.articleReview.dto;

import com.jupiter.web.manager.common.base.PageOutputDto;
import lombok.Data;

/**
 * @author fuyuling
 * @created 2019/7/23 18:24
 */
@Data
public class ArticleReplyQueryOutputDto {

    private String review_id;
    private Long timestamp; // 查询数据的时间戳，上拉加载下一页时由客户端传入，并只查询该时间以前的数据
    private PageOutputDto<ArticleReplyDto> replies;

}
