package com.jupiter.web.manager.common.provider;

import com.jupiter.web.manager.bus.articleCategory.dto.ArticleCategoryDto;
import org.springframework.util.StringUtils;

public class ArticlesCategoryProvider {

    public String searchAll(ArticleCategoryDto categoryDto) {

       StringBuffer result = new StringBuffer("select * from jpt_article_category where delete_flag = 0 ");
       if (!StringUtils.isEmpty(categoryDto.getCategoryName())){
           result.append(" and name like  CONCAT(CONCAT('%', #{categoryName}), '%')");
       }
       result.append(" ORDER BY seq asc ");
       return result.toString();
    }

}
