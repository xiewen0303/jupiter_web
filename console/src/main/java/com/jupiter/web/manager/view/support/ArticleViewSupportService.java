package com.jupiter.web.manager.view.support;

import com.jupiter.web.manager.bus.article.dto.request.GoodsCardInfoRequest;
import com.jupiter.web.manager.bus.article.dto.response.GoodsCardInfoViewResponse;

/**
 * Created by zhangxiqiang on 2019/7/29.
 */
public interface ArticleViewSupportService {

    GoodsCardInfoViewResponse getGoodsCardInfoViewResponse(GoodsCardInfoRequest goodsCardInfoRequest);
}
