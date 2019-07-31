package com.jupiter.web.manager.bus.articleCategory.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jupiter.web.manager.bus.admin.dto.CommonResponse;
import com.jupiter.web.manager.bus.articleCategory.dto.ArticleCategoryDto;
import com.jupiter.web.manager.bus.articleCategory.service.ArticleCategoryService;
import com.jupiter.web.manager.common.entity.ArticleCategoryEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("articleCategory")
public class ArticleCategoryController {

    @Resource
    private ArticleCategoryService articleCategoryService;

    @RequestMapping("/search")
    public String transfer(Model model,ArticleCategoryDto categoryDto) {
        List<ArticleCategoryEntity> articlesCategorys = articleCategoryService.getCategoryInfoPages(categoryDto);
        CommonResponse<PageInfo<ArticleCategoryEntity>> result = new CommonResponse<>();
        PageInfo<ArticleCategoryEntity> pageInfo = new PageInfo<>(articlesCategorys);
        result.setModule(pageInfo);
        model.addAttribute("categoryLists", result);
        return "articleCategory/categoryList";
    }

    @RequestMapping(value = "/addCategory")
    public String addCategory(Model model) {
        return "articleCategory/addCategory";
    }

    @ResponseBody
    @RequestMapping(value = "/submitCategory")
    public CommonResponse addCategory(ArticleCategoryEntity categoryEntity){
        return articleCategoryService.addCategory(categoryEntity);
    }

    @ResponseBody
    @RequestMapping(value = "/getCategoryInfoPages")
    public CommonResponse<PageInfo<ArticleCategoryEntity>> getCategoryInfoPages(ArticleCategoryDto categoryDto) {

        if (!StringUtils.isEmpty(categoryDto.getPage()) && !StringUtils.isEmpty(categoryDto.getRows())) {
            PageHelper.startPage(categoryDto.getPage(), categoryDto.getRows());
        }
        List<ArticleCategoryEntity> articlesCategorys = articleCategoryService.getCategoryInfoPages(categoryDto);

        PageInfo<ArticleCategoryEntity> pageInfo = new PageInfo<>(articlesCategorys);
        CommonResponse<PageInfo<ArticleCategoryEntity>> result = new CommonResponse<>();
        result.setModule(pageInfo);
        result.setCode("200");
        result.setMsg("success");
        return result;
    }


    @ResponseBody
    @RequestMapping(value = "/updateCategory")
    public CommonResponse updateCategory(String categoryId,int type) {
       return  articleCategoryService.updateCategory(categoryId,type);
    }


    @ResponseBody
    @RequestMapping(value = "/subModifyCategory")
    public CommonResponse subModifyCategory(ArticleCategoryEntity categoryEntity) {
       return articleCategoryService.subModifyCategory(categoryEntity);
    }

    @RequestMapping(value = "/modifyCategory")
    public String modifyCategory(Model model,String categoryId) {
        if(StringUtils.isEmpty(categoryId)) {
            log.error("唯一id不能为空！");
            return "articleCategory/modifyCategory";
        }

//        ArticleCategoryEntity params = new ArticleCategoryEntity();
//        params.setId(categoryId);
        ArticleCategoryEntity oldCategoryEntity = articleCategoryService.getCategoryEntityById(categoryId);//getCategoryEntityLastOne(params);
        if(oldCategoryEntity == null){
            log.error("需要修改的类目数据不存在!");
            return "articleCategory/modifyCategory";
        }

        model.addAttribute("categoryEntity",oldCategoryEntity);
        return "articleCategory/modifyCategory";
    }

}

