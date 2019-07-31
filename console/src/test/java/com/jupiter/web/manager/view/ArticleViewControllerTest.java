package com.jupiter.web.manager.view;

import com.jupiter.web.manager.bus.article.dto.request.GoodsCardInfoRequest;
import com.jupiter.web.manager.bus.article.dto.request.GoodsCardLinkRequest;
import com.tron.common.util.BeanUtils;
import com.tron.common.util.HttpClientUtils;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by zhangxiqiang on 2019/7/29.
 */
public class ArticleViewControllerTest {
    @Test
    public void getGoodsCardInfo() throws Exception {
        String url = "http://localhost:8011/jupiter/api/web/view/article/get_goods_card_info";
        GoodsCardInfoRequest goodsCardInfoRequest = new GoodsCardInfoRequest();
        goodsCardInfoRequest.setArticle_goods_id("499b11c092cb4cacb5cce45717e1bcd8");
        goodsCardInfoRequest.setTemplate_id("12309450917611e99b44080027e11242");
        Map params = BeanUtils.request2Map(goodsCardInfoRequest);
        String s = HttpClientUtils.doGet(url, params);
        System.out.println(s);
    }
}