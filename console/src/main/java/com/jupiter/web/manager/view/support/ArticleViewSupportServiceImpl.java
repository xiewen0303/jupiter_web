package com.jupiter.web.manager.view.support;

import com.jupiter.web.manager.bus.article.dto.request.GoodsCardInfoRequest;
import com.jupiter.web.manager.bus.article.dto.response.GoodsCardInfoViewResponse;
import com.jupiter.web.manager.bus.article.service.ArticleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by zhangxiqiang on 2019/7/29.
 */
@Service
public class ArticleViewSupportServiceImpl implements ArticleViewSupportService {
    @Resource
    private ArticleService articleService;

    @Override
    public GoodsCardInfoViewResponse getGoodsCardInfoViewResponse(GoodsCardInfoRequest goodsCardInfoRequest) {
        return articleService.getGoodsCardInfo(goodsCardInfoRequest);
    }
}
