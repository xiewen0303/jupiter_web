package com.jupiter.web.manager.common.entity;

import com.tron.common.mysql.mybatis.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Table;

@Data
@Table(name = "category_lable")
public class LableGoodsEntity extends BaseEntity<Long> {
    //标签ID了，
    private Long labelId;
    //商品Id
    private String goodsId;
}