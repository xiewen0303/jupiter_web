package com.jupiter.web.manager.view;

import com.jupiter.web.manager.bus.article.dto.request.GoodsCardInfoRequest;
import com.jupiter.web.manager.bus.article.dto.response.GoodsCardInfoViewResponse;
import com.jupiter.web.manager.common.locale.DefaultLocale;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * Created by zhangxiqiang on 2019/7/29.
 */
@Slf4j
@Controller
@RequestMapping("/jupiter/api/web/view/article")
public class ArticleViewController {
    @Resource
    private ArticleViewService articleViewService;

    /**
     * 获取商户卡片信息
     *
     * @return
     */
    @GetMapping("/get_goods_card_info")
    public String getGoodsCardInfo(Model model, GoodsCardInfoRequest goodsCardInfoRequest) {
        Objects.requireNonNull(goodsCardInfoRequest, DefaultLocale.get("params_error"));
        Objects.requireNonNull(goodsCardInfoRequest.getArticle_goods_id(), DefaultLocale.get("params_error"));
        Objects.requireNonNull(goodsCardInfoRequest.getTemplate_id(), DefaultLocale.get("params_error"));
        GoodsCardInfoViewResponse response =  articleViewService.getGoodsCardInfo(goodsCardInfoRequest);
        model.addAttribute("goods_card_info", response.getResponse());
        return response.getPath();
    }
}
