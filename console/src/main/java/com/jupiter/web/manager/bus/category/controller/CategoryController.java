package com.jupiter.web.manager.bus.category.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jupiter.web.manager.bus.admin.dto.CommonResponse;
import com.jupiter.web.manager.bus.category.dto.CategoryDto;
import com.jupiter.web.manager.bus.category.dto.CategoryShowInfos;
import com.jupiter.web.manager.common.dao.CategoryDao;
import com.jupiter.web.manager.common.dao.CategoryMappingDao;
import com.jupiter.web.manager.common.entity.CategoryEntity;
import com.jupiter.web.manager.common.entity.CategoryMappingEntity;
import com.jupiter.web.manager.common.entity.SouceSiteEntity;
import com.jupiter.web.manager.utils.GenIdTools;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("category")
public class CategoryController {

    @Resource
    private CategoryDao categoryDao;

    @Resource
    private CategoryMappingDao categoryMappingDao;

    @RequestMapping("/search")
    public String transfer(Model model,CategoryDto categoryDto) {
        CommonResponse<PageInfo<CategoryEntity>> result =  getCategoryInfoPages(categoryDto);
        model.addAttribute("categoryLists", result);
        return "category/categoryList";
    }

    @RequestMapping(value = "/addCategory")
    public String addCategory(Model model) {
        List<SouceSiteEntity> sourceSites =  categoryDao.getSourceSite();
        model.addAttribute("sourceSites", sourceSites);
        return "category/addCategory";
    }

    @ResponseBody
    @RequestMapping(value = "/submitCategory")
    public CommonResponse submitCategory(CategoryEntity categoryEntity){
        CommonResponse result = new CommonResponse<>();
        if(StringUtils.isEmpty(categoryEntity.getName())){
            result.setCode("400");
            result.setMsg("分类名字不能为空！");
            log.error("分类名字不能为空！");
            return result;
        }

        CategoryEntity old = categoryDao.getNoDeleteCategoryEntity(categoryEntity.getCode());
        if(old != null) {
            result.setCode("401");
            result.setMsg("分类唯一标识已存在！");
            log.error("分类唯一标识已存在！");
            return result;
        }

        if(categoryEntity.getMappings() == null || categoryEntity.getMappings().isEmpty()){
            result.setCode("402");
            result.setMsg("分类关联地址不能少于一个！");
            log.error("分类关联地址不能少于一个！");
            return result;
        }


        List<CategoryMappingEntity> categoryMappings = new ArrayList<>();
        List<String> origins  = categoryEntity.getOrigin();
        int i = 0;
        for (String mappingUrl : categoryEntity.getMappings()) {
            String origin = origins.get(i++);

            if(StringUtils.isEmpty(mappingUrl) || StringUtils.isEmpty(origin) || "-1".equals(origin)){
                continue;
            }

            if(!mappingUrl.contains(origin)){
                result.setCode("403");
                result.setMsg("爬取地址的url与网站不符,网站=【"+origin+"】url="+mappingUrl);
                log.error("爬取地址的url与网站不符");
                return result;
            }

            CategoryMappingEntity entity = new CategoryMappingEntity();
            entity.setId(GenIdTools.getUUId());
            entity.setCode(categoryEntity.getCode());
            entity.setOrigin(origin);
            entity.setUrl(mappingUrl);
            entity.setDeleteFlag(0);
            categoryMappings.add(entity);
        }

        if(categoryMappings.size() == 0){
            result.setCode("403");
            result.setMsg("分类关联地址不能少于一个！");
            return result;
        }

        if(StringUtils.isEmpty(categoryEntity.getId())) {
            categoryEntity.setId(GenIdTools.getUUId());
            categoryEntity.setAddTime(System.currentTimeMillis());
            categoryEntity.setUpdateTime(System.currentTimeMillis());
            categoryEntity.setDeleteFlag(0);
            categoryEntity.setAddTime(System.currentTimeMillis());
            categoryEntity.setUpdateTime(System.currentTimeMillis());
            categoryEntity.setEnabled(0);
            categoryDao.insert(categoryEntity);
        }

        for (CategoryMappingEntity entity : categoryMappings) {
            categoryMappingDao.insert(entity);
        }

        result.setCode("200");
        result.setMsg("提交成功");
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/getCategoryInfoPages")
    public CommonResponse<PageInfo<CategoryEntity>> getCategoryInfoPages(CategoryDto categoryDto) {

        if (!StringUtils.isEmpty(categoryDto.getPage()) && !StringUtils.isEmpty(categoryDto.getRows())) {
            PageHelper.startPage(categoryDto.getPage(), categoryDto.getRows());
        }
        CommonResponse<PageInfo<CategoryEntity>> result = new CommonResponse<>();
        List<CategoryEntity> list = categoryDao.searchAll(categoryDto);
        if(list != null){
            for (CategoryEntity categoryEntity : list) {
                String code = categoryEntity.getCode();
                CategoryMappingEntity queryMapping = new CategoryMappingEntity();
                queryMapping.setCode(code);
                queryMapping.setDeleteFlag(0);
                List<CategoryMappingEntity> categoryMappingEntities = categoryMappingDao.get(queryMapping);

                List<String> mappings = null;
                if((mappings = categoryEntity.getMappings()) == null){
                    mappings = new ArrayList<>();
                    categoryEntity.setMappings(mappings);
                }
                for (CategoryMappingEntity categoryMappingEntity : categoryMappingEntities) {
                    mappings.add(categoryMappingEntity.getUrl());
                }
            }
        }

        PageInfo<CategoryEntity> pageInfo = new PageInfo<>(list);
        result.setModule(pageInfo);
        result.setCode("200");
        result.setMsg("success");
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/getCategoryInfos",produces="application/json;charset=utf-8")
    public CommonResponse getCategoryInfos(){
        CommonResponse<CategoryShowInfos> result = new CommonResponse<>();

        List<CategoryEntity> categoryEntitys = categoryDao.getAll();

        List<CategoryShowInfos.CategoryShowInfo> data = getMenuTree(categoryEntitys,"");
        CategoryShowInfos categoryShowInfos = new CategoryShowInfos();
        categoryShowInfos.setData(data);

        result.setModule(categoryShowInfos);
        result.setCode("200");
        result.setMsg("success");
        return result;
    }

    /**
     * 递归取出所有关系树
     * @param list
     * @param pid
     * @return
     */
    private List<CategoryShowInfos.CategoryShowInfo> getMenuTree(List<CategoryEntity> list,String pid) {
        List<CategoryShowInfos.CategoryShowInfo> menuList=new ArrayList<>();
        //取出所有菜单
        for (int i = 0;i< list.size();i++) {
            CategoryEntity categoryEntity = list.get(i);
            if (pid.equals(categoryEntity.getParentCode())) {//取出所有父菜单
                CategoryShowInfos.CategoryShowInfo showInfo = coverCategoryShowInfo(categoryEntity);
                showInfo.setChildren(getMenuTree(list, showInfo.getId()));
                menuList.add(showInfo);
            }
        }
        return menuList;
    }

    public CategoryShowInfos.CategoryShowInfo coverCategoryShowInfo(CategoryEntity categoryEntity) {
        return  CategoryShowInfos.CategoryShowInfo.builder().
                id(categoryEntity.getCode()).
                parentId(categoryEntity.getParentCode()).
                text(categoryEntity.getName()).
                icon("none").build();
    }

    @ResponseBody
    @RequestMapping(value = "/updateCategory")
    public CommonResponse updateCategory(String categoryId,int type) {
        CommonResponse  result = new CommonResponse();
        if(StringUtils.isEmpty(categoryId)){
            result.setCode("201");
            result.setMsg("唯一id不能为空！");
            return result;
        }

        CategoryEntity params = new CategoryEntity();
        params.setId(categoryId);
        CategoryEntity oldEntity = categoryDao.getLastOne(params);
        if(oldEntity == null){
            result.setCode("201");
            result.setMsg("操作失败,数据库没有该数据!");
            return result;
        }

        switch (type){
            case 1: //删除
                params.setDeleteFlag(1);
                categoryMappingDao.deleteCategoryMapping(oldEntity.getCode());
                break;
            case 2: //下架
                params.setEnabled(0);
                break;
            case 3: //上架
                params.setEnabled(1);
                break;
            default:
                log.error("更新category类型不支持,type={}",type);
                result.setCode("201");
                result.setMsg("操作失败,更新category类型不支持！");
                return result;
        }

        categoryDao.update(params);

        result.setCode("200");
        result.setMsg("操作成功！");
        return result;
    }


    @ResponseBody
    @RequestMapping(value = "/subModifyCategory")
    public CommonResponse modifyCategory(CategoryEntity categoryEntity) {
        CommonResponse result = new CommonResponse();

        //更新Category
        CategoryEntity category = new CategoryEntity();
        category.setId(categoryEntity.getId());
        category = categoryDao.getLastOne(category);
        if(category == null){
            result.setCode("201");
            result.setMsg("操作失败,数据库没有该数据!");
            return result;
        }

        category.setName(categoryEntity.getName());
        category.setSeq(categoryEntity.getSeq());
        categoryDao.update(category);

        //更新CategoryMapping
        List<String> mappings = categoryEntity.getMappings();
        List<String> origins = categoryEntity.getOrigin();
        List<String> mappingIds = categoryEntity.getMappingIds();
        for(int i = 0; i < origins.size(); i++) {
            String origin = origins.get(i);
            String mapping = mappings.get(i);

            if(!"-1".equals(origin)  && !StringUtils.isEmpty(mapping)){

                if(!mapping.contains(origin)){
                    result.setCode("403");
                    result.setMsg("爬取地址的url与网站不符,网站=【"+origin+"】url="+mapping);
                    log.error("爬取地址的url与网站不符,网站=【{}】url={}",origin,mapping);
                    return result;
                }

                if(mappingIds.size() < i+1 ){ //新增
                    CategoryMappingEntity entity = new CategoryMappingEntity();
                    entity.setId(GenIdTools.getUUId());
                    entity.setCode(categoryEntity.getCode());
                    entity.setOrigin(origin);
                    entity.setUrl(mapping);
                    categoryMappingDao.insert(entity);
                }else{ //修改
                    CategoryMappingEntity queryMapping = new CategoryMappingEntity();
                    queryMapping.setId(mappingIds.get(i));
                    CategoryMappingEntity oldCategoryMapping = categoryMappingDao.getLastOne(queryMapping);
                    if(oldCategoryMapping == null){
                        log.error("该数据异常,CategoryMappingId={},CategoryId={}",mappingIds.get(i),categoryEntity.getId());
                        continue;
                    }
                    oldCategoryMapping.setOrigin(origin);
                    oldCategoryMapping.setUrl(mapping);
                    categoryMappingDao.update(oldCategoryMapping);
                }
            }else{
                if(mappingIds.size() > i) { //删除
                    CategoryMappingEntity queryMapping = new CategoryMappingEntity();
                    queryMapping.setId(mappingIds.get(i));
                    queryMapping.setDeleteFlag(1);
                    categoryMappingDao.update(queryMapping);
                }
            }
        }

        result.setCode("200");
        result.setMsg("操作成功！");
        return result;
    }



    @RequestMapping(value = "/modifyCategory")
    public String modifyCategory(Model model,String categoryId) {
        if(StringUtils.isEmpty(categoryId)) {
            log.error("唯一id不能为空！");
            return "category/modifyCategory";
        }
        CategoryEntity params = new CategoryEntity();
        params.setId(categoryId);
        CategoryEntity oldCategoryEntity = categoryDao.getFirstOne(params);
        if(oldCategoryEntity == null){
            log.error("需要修改的类目数据不存在!");
            return "category/modifyCategory";
        }

        CategoryMappingEntity queryMappings = new CategoryMappingEntity();
        queryMappings.setCode(oldCategoryEntity.getCode());
        List<CategoryMappingEntity> categoryMappingEntity = categoryMappingDao.get(queryMappings);
        oldCategoryEntity.setCategoryMappings(categoryMappingEntity);
        model.addAttribute("categoryEntity",oldCategoryEntity);
        List<SouceSiteEntity> sourceSites =  categoryDao.getSourceSite();
        model.addAttribute("sourceSites", sourceSites);

        return "category/modifyCategory";
    }


}

