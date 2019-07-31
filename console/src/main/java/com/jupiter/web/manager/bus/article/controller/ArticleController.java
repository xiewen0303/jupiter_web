package com.jupiter.web.manager.bus.article.controller;

import com.jupiter.web.aop.LoginUser;
import com.jupiter.web.manager.bus.article.dto.request.*;
import com.jupiter.web.manager.bus.article.service.ArticleService;
import com.jupiter.web.manager.bus.articleBanner.service.ArticleBannerService;
import com.jupiter.web.manager.common.entity.User;
import com.jupiter.web.manager.common.enums.ArticleInfoType;
import com.jupiter.web.manager.common.locale.DefaultLocale;
import com.tron.framework.dto.ResponseEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Objects;

/**
 * Created by zhangxiqiang on 2019/7/22.
 */
@Slf4j
@RestController
@RequestMapping("/jupiter/api/web/article")
public class ArticleController {
    @Autowired
    private ArticleService articleService;
    @Autowired
    private ArticleBannerService articleBannerService;

    /**
     * 根据url查询商品明细
     *
     * @return
     */
    @GetMapping("/get_goods_card_link")
    public ResponseEntity getGoodsCardLink(GoodsCardLinkRequest goodsCardInfoDto) throws Exception {
        Objects.requireNonNull(goodsCardInfoDto, DefaultLocale.get("params_error"));
        Objects.requireNonNull(goodsCardInfoDto.getUrl(), DefaultLocale.get("params_error"));
        goodsCardInfoDto.setUrl(goodsCardInfoDto.getUrl().trim());
        return articleService.getGoodsCardLink(goodsCardInfoDto);
    }


    /**
     * 保存和发布
     *
     * @param articleInfoDto
     * @return
     */
    @PostMapping("/update")
    public ResponseEntity update(ArticleInfoRequest articleInfoDto, @LoginUser User user) {
        Objects.requireNonNull(articleInfoDto, DefaultLocale.get("params_error"));
        Objects.requireNonNull(articleInfoDto.getType(), DefaultLocale.get("params_error"));
        Objects.requireNonNull(articleInfoDto.getTitle(), DefaultLocale.get("params_error"));
        if (articleInfoDto.getType().equals(ArticleInfoType.PUBLISH.getCode())) {
            Objects.requireNonNull(articleInfoDto.getContent(), DefaultLocale.get("params_error"));
            Objects.requireNonNull(articleInfoDto.getHead_pic(), DefaultLocale.get("params_error"));
            Objects.requireNonNull(articleInfoDto.getTitle(), DefaultLocale.get("params_error"));
        }

        return articleService.update(articleInfoDto, user);
    }

    /**
     * 根据文章id查询某个文章信息
     *
     * @param article_id
     * @return
     */
    @GetMapping("/get")
    public ResponseEntity get(String article_id, @LoginUser(require = false) User user) {
        Objects.requireNonNull(article_id, DefaultLocale.get("params_error"));
        return articleService.get(article_id, user);
    }

    /**
     * 删除文章
     *
     * @param user
     * @return
     */
    @GetMapping("/remove")
    public ResponseEntity remove(String article_id, @LoginUser User user) throws Exception {
        Objects.requireNonNull(article_id, DefaultLocale.get("params_error"));
        return articleService.remove(article_id, user);
    }

    /**
     * 获取所有的文章类别
     *
     * @return
     */
    @GetMapping("/category")
    public ResponseEntity getCategory() {
        return articleService.getCategory();
    }

    /**
     * 点赞与取消点赞
     */
    @GetMapping("/like")
    public ResponseEntity like(ArticleLikeRequest articleLikeRequest, @LoginUser User user) {
        Objects.requireNonNull(articleLikeRequest, DefaultLocale.get("params_error"));
        Objects.requireNonNull(articleLikeRequest.getArticle_id(), DefaultLocale.get("params_error"));
        Objects.requireNonNull(articleLikeRequest.getLike(), DefaultLocale.get("params_error"));
        return articleService.like(articleLikeRequest, user);
    }

    /**
     * 获取所有的文章列表
     *
     * @param articleListRequest
     * @return
     */
    @GetMapping("/get_all")
    public ResponseEntity getAll(ArticleListRequest articleListRequest, @LoginUser(require = false) User user) {
        Objects.requireNonNull(articleListRequest, DefaultLocale.get("params_error"));
        return articleService.getAll(articleListRequest, user);
    }

    @GetMapping("/get_publish_info")
    public ResponseEntity getPublishInfo(PublishInfoRequest publishInfoRequest, @LoginUser User user) {
        Objects.requireNonNull(publishInfoRequest, DefaultLocale.get("params_error"));
        return articleService.getPublishInfo(publishInfoRequest, user);
    }
}