package com.jupiter.web.manager.bus.article.dto.request;

import com.jupiter.web.manager.bus.admin.dto.BaseDto;
import lombok.Data;

/**
 * Created by zhangxiqiang on 2019/7/23.
 */
@Data
public class ArticleListRequest  extends BaseDto{
    private String category_id;
    private Long timestamp;  //区分最大时间
}
