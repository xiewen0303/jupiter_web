package com.jupiter.web.manager.bus.article.dto.response;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by zhangxiqiang on 2019/7/23.
 */
@Data
public class GoodsCardInfoResponse {
    private String title;
    private BigDecimal price;
    private BigDecimal origin_price;
    private String icon;
    private String price_unit;
    private BigDecimal discount;
    private BigDecimal score;
    private String url;
    private String origin;
}
