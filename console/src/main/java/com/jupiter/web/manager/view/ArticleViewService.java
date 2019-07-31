package com.jupiter.web.manager.view;

import com.jupiter.web.manager.bus.article.dto.request.GoodsCardInfoRequest;
import com.jupiter.web.manager.bus.article.dto.response.GoodsCardInfoViewResponse;
import com.jupiter.web.manager.view.support.ArticleViewSupportService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by zhangxiqiang on 2019/7/29.
 */
@Service
public class ArticleViewService {

    @Resource
    private ArticleViewSupportService articleViewSupportService;

    public GoodsCardInfoViewResponse getGoodsCardInfo(GoodsCardInfoRequest goodsCardInfoRequest) {
        return articleViewSupportService.getGoodsCardInfoViewResponse(goodsCardInfoRequest);
    }
}
