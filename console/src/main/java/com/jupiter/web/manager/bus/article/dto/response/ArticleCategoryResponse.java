package com.jupiter.web.manager.bus.article.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by zhangxiqiang on 2019/7/23.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleCategoryResponse {
    private String name;
    private String category_id;
}
