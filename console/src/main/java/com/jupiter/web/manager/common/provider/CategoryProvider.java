package com.jupiter.web.manager.common.provider;

import com.jupiter.web.manager.bus.category.dto.CategoryDto;
import org.springframework.util.StringUtils;

public class CategoryProvider {

    public String searchAll(CategoryDto categoryDto) {

       StringBuffer result = new StringBuffer("select * from jpt_goods_category where delete_flag = 0 ");
       if (!StringUtils.isEmpty(categoryDto.getCategoryName())){
           result.append(" and name like  CONCAT(CONCAT('%', #{categoryName}), '%')");
       }
       result.append(" ORDER BY seq asc ");
       return result.toString();
    }

    public String getSourceSite() {
        return "select * from jpt_source_site where enabled = 1";
    }

}
