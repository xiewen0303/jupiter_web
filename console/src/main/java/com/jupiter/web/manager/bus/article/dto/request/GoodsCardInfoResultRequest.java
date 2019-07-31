package com.jupiter.web.manager.bus.article.dto.request;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by zhangxiqiang on 2019/7/23.
 */
@Data
public class GoodsCardInfoResultRequest {
    private String billNo;
    private String origin;
    private String title;
    private String icon;
    private BigDecimal price;
    private String price_unit;
    private BigDecimal origin_price;
    private BigDecimal discount;
    private BigDecimal score;
}
