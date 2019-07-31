package com.jupiter.web.manager.common.provider;

import com.jupiter.web.manager.bus.category.dto.CategoryDto;
import org.springframework.util.StringUtils;

public class LabelProvider {

    public String search(CategoryDto roleDto) {
        StringBuffer result = new StringBuffer(" select l.*,c.`name` as category_name from label l,category_label c_l,jpt_goods_category c where l.id = c_l.label_id and c_l.category_id = c.code ");
                if(!StringUtils.isEmpty(roleDto.getCategoryName())) {
                    result.append(" and c_l.category_id in(#{categoryName})");
                }
        return result.toString();
    }
}
