package com.jupiter.web.manager.common.entity;

import com.tron.common.mysql.mybatis.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Table;

@Data
@Table(name = "category_label")
public class CategoryLabelEntity extends BaseEntity<String> {

    //标签Id
    private String labelId;
    //类目Id
    private String categoryId;
}
