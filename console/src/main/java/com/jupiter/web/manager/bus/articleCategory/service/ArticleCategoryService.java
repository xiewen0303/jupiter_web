package com.jupiter.web.manager.bus.articleCategory.service;

import com.jupiter.web.manager.bus.admin.dto.CommonResponse;
import com.jupiter.web.manager.bus.articleCategory.dto.ArticleCategoryDto;
import com.jupiter.web.manager.common.dao.ArticleCategoryDao;
import com.jupiter.web.manager.common.entity.ArticleCategoryEntity;
import com.jupiter.web.manager.common.entity.BannerEntity;
import com.jupiter.web.manager.utils.GenIdTools;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Service
public class ArticleCategoryService {

    @Resource
    private ArticleCategoryDao articlesCategoryDao;

    public List<ArticleCategoryEntity> getAll() {
        return articlesCategoryDao.getAll();
    }

    /**
     * 新增label
     * @param entity
     */

    public String insertLabel(ArticleCategoryEntity entity) {
        int i = articlesCategoryDao.insert(entity);
        return i > 0 ? entity.getId() : null;
    }

    /**
     * 修改label
     * @param entity
     */

    public boolean updateLabel(ArticleCategoryEntity entity) {
        return articlesCategoryDao.update(entity) > 0;
    }

    /**
     * 根据ID查询label
     * @param id
     */
    public ArticleCategoryEntity getCategoryEntityById(String id) {
        return articlesCategoryDao.getById(id);
    }

    public  List<ArticleCategoryEntity> getCategoryInfoPages(ArticleCategoryDto categoryDto) {
        return articlesCategoryDao.searchAll(categoryDto);
    }

    public  List<ArticleCategoryEntity> getCategoryInfos() {
        ArticleCategoryEntity entity = new ArticleCategoryEntity();
        entity.setEnabled(1);
        entity.setDeleteFlag(0);
        return articlesCategoryDao.get(entity);
    }

    /**
     * 添加类目
     * @param categoryEntity
     * @return
     */
    public CommonResponse addCategory(ArticleCategoryEntity categoryEntity) {
        CommonResponse result = new CommonResponse();
        if(StringUtils.isEmpty(categoryEntity.getName())){
            result.setCode("400");
            result.setMsg("分类名字不能为空！");
            log.error("分类名字不能为空！");
            return result;
        }

        ArticleCategoryEntity queryParams = new ArticleCategoryEntity();
        queryParams.setCode(categoryEntity.getCode());
        ArticleCategoryEntity old =   articlesCategoryDao.getLastOne(queryParams);
        if(old != null) {
            result.setCode("401");
            result.setMsg("分类唯一标识已存在！");
            log.error("分类唯一标识已存在！");
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
            articlesCategoryDao.insert(categoryEntity);
        }

        result.setCode("200");
        result.setMsg("提交成功");
        return result;
    }

    /**
     * 列表页面操作
     * @param categoryId
     * @param type
     * @return
     */
    public CommonResponse updateCategory(String categoryId, int type) {
        CommonResponse  result = new CommonResponse();
        if(StringUtils.isEmpty(categoryId)){
            result.setCode("201");
            result.setMsg("唯一id不能为空！");
            return result;
        }

        ArticleCategoryEntity params = new ArticleCategoryEntity();
        params.setId(categoryId);
        ArticleCategoryEntity oldEntity = articlesCategoryDao.getLastOne(params);
        if(oldEntity == null){
            result.setCode("201");
            result.setMsg("操作失败,数据库没有该数据!");
            return result;
        }

        switch (type){
            case 1: //删除
                params.setDeleteFlag(1);
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

        articlesCategoryDao.update(params);
        result.setCode("200");
        result.setMsg("操作成功！");
        return result;
    }

    public CommonResponse subModifyCategory(ArticleCategoryEntity categoryEntity) {
        CommonResponse result = new CommonResponse();

        //更新Category
        ArticleCategoryEntity category = new ArticleCategoryEntity();
        category.setId(categoryEntity.getId());
        category = articlesCategoryDao.getLastOne(category);
        if(category == null){
            result.setCode("201");
            result.setMsg("操作失败,数据库没有该数据!");
            return result;
        }

        category.setName(categoryEntity.getName());
        category.setSeq(categoryEntity.getSeq());
        articlesCategoryDao.update(category);

        result.setCode("200");
        result.setMsg("操作成功！");
        return result;
    }
}
