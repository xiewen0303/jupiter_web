package com.jupiter.web.manager.bus.article.controller;

import com.github.pagehelper.PageInfo;
import com.jupiter.web.manager.bus.admin.dto.CommonResponse;
import com.jupiter.web.manager.bus.article.dto.ArticleManagerDto;
import com.jupiter.web.manager.bus.article.dto.request.ArticleInfoReq;
import com.jupiter.web.manager.bus.article.dto.response.ArticleManagerResponse;
import com.jupiter.web.manager.bus.article.service.ArticleManagerService;
import com.jupiter.web.manager.bus.article.service.ArticleService;
import com.jupiter.web.manager.bus.articleCategory.service.ArticleCategoryService;
import com.jupiter.web.manager.bus.articleReview.dto.ArticleReviewDto;
import com.jupiter.web.manager.bus.articleReview.dto.ArticleReviewQueryInputDto;
import com.jupiter.web.manager.bus.articleReview.service.ArticleReviewService;
import com.jupiter.web.manager.common.entity.ArticleCategoryEntity;
import com.jupiter.web.manager.common.entity.ArticleContentMongoEntity;
import com.jupiter.web.manager.common.entity.ArticleInfoEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

/**
 * Created by zhangxiqiang on 2019/7/22.
 */
@Slf4j
@RequestMapping("/articleInfo")
@Controller
public class ArticleManagerController {

    @Resource
    private ArticleManagerService articleManagerService;

    @Resource
    private ArticleCategoryService articleCategoryService;
    @Resource
    private ArticleService articleService;

    @Resource
    private ArticleReviewService articleReviewService;

    @RequestMapping(value = "/addArticle")
    public String addBanner(){
        return "articleInfo/addArticle";
    }

    @ResponseBody
    @RequestMapping(value = "/submitSearch")
    public CommonResponse submitBanner(ArticleManagerDto articleManagerDto) throws IOException {
        CommonResponse<PageInfo<ArticleInfoEntity>> result = new CommonResponse<>();
        List<ArticleInfoEntity> articleCategoryEntityList = articleManagerService.submitBanner(articleManagerDto);
        PageInfo<ArticleInfoEntity> pageInfo = new PageInfo<>(articleCategoryEntityList);
        result.setModule(pageInfo);
        result.setCode("200");
        result.setMsg("success");
        return result;
    }

    @RequestMapping(value = "/search")
    public String search(Model model) {
        List<ArticleCategoryEntity>  articleCategoryEntities = articleCategoryService.getCategoryInfos();
        model.addAttribute("categorys",articleCategoryEntities);
        return "articleInfo/articleList";
    }

    @RequestMapping(value = "/articleInfo")
    public String articleInfo(Model model,String id) {
        ArticleInfoEntity entity = articleManagerService.loadLastArticleInfo(id);
        if(null == entity){
            log.error("articleInfo数据不存在，id={}",id);
        }

        ArticleManagerResponse respone = new ArticleManagerResponse();
        respone.setArticleInfo(entity);

        int commentsCount = articleManagerService.getCommentsCount(id);
        int likeTotal = articleManagerService.getLikeCount(id);
        respone.setCommentTotal(commentsCount);
        respone.setLikeTotal(likeTotal);

        ArticleReviewQueryInputDto params = new ArticleReviewQueryInputDto();
        params.setArticle_id(id);
        params.setTimestamp(System.currentTimeMillis());
        List<ArticleReviewDto> articleReviewResult = articleReviewService.getReviews(params);
        respone.setArticleReviewDtos(articleReviewResult);

        model.addAttribute("articleInfo",respone);
        return "articleInfo/info";
    }


    @RequestMapping(value = "/content")
    public String getContent(Model model,String id) {
        ArticleContentMongoEntity articleContentMongoEntity = articleService.getArticleContentById(id);

        if(articleContentMongoEntity != null){
            model.addAttribute("content",articleContentMongoEntity.getData());
        }
        return "articleInfo/content";
    }

    @ResponseBody
    @RequestMapping(value = "/auditAricleInfo")
    public CommonResponse auditAricleInfo(ArticleManagerDto articleManagerDto) throws IOException {
        CommonResponse<PageInfo<ArticleInfoEntity>> result = new CommonResponse<>();
        int flag = articleManagerService.rejectAricleInfo(articleManagerDto);
        if(flag > 0){
            result.setCode("200");
            result.setMsg("success");
        }else{
            result.setCode("201");
            result.setMsg("更新失败！");
        }
        return result;
    }


    @RequestMapping(value = "/searchOfficial")
    public String searchOfficial(Model model) {
        List<ArticleCategoryEntity>  articleCategoryEntities = articleCategoryService.getCategoryInfos();
        model.addAttribute("categorys",articleCategoryEntities);
        return "articleInfo/officialArticleList";
    }

    @ResponseBody
    @RequestMapping(value = "/updateOfficiaArticle")
    public CommonResponse updateOfficiaArticle(String id,int type) {
       return articleManagerService.updateOfficiaArticle(id,type);
    }

    @RequestMapping(value = "/editorArticleInfo")
    public String editorArticleInfo(Model model,String id) {
        ArticleInfoEntity entity = articleManagerService.loadLastArticleInfo(id);
        if(null == entity){
            log.error("articleInfo数据不存在，id={}",id);
        }
        model.addAttribute("articleInfo",entity);

        List<ArticleCategoryEntity>  articleCategoryEntities = articleCategoryService.getCategoryInfos();
        model.addAttribute("categorys",articleCategoryEntities);
        return "articleInfo/modifyArticle";
    }

    @ResponseBody
    @RequestMapping(value = "/subArticleInfo")
    public CommonResponse subArticleInfo(ArticleInfoReq req) {
        return articleManagerService.modifyArticleInfo(req);
    }
}