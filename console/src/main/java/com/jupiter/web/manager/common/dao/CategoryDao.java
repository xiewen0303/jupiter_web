package com.jupiter.web.manager.common.dao;

import com.jupiter.web.manager.bus.category.dto.CategoryDto;
import com.tron.common.mysql.mybatis.dao.CommonDao;
import com.jupiter.web.manager.common.entity.BannerEntity;
import com.jupiter.web.manager.common.entity.CategoryEntity;
import com.jupiter.web.manager.common.entity.SouceSiteEntity;
import com.jupiter.web.manager.common.provider.BannerProvider;
import com.jupiter.web.manager.common.provider.CategoryProvider;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryDao extends CommonDao<CategoryEntity, String> {

    @Override
    default Class<CategoryEntity> getSelfClass() {
        return CategoryEntity.class;
    }

    @SelectProvider(type = CategoryProvider.class, method = "searchAll")
    List<CategoryEntity> searchAll(CategoryDto categoryDto);

    @SelectProvider(type = CategoryProvider.class, method = "getSourceSite")
    List<SouceSiteEntity> getSourceSite();


    @Select("select * from jpt_goods_category where delete_flag = 0 and `code` = #{code}  LIMIT 1 ")
    CategoryEntity getNoDeleteCategoryEntity(String code);
}
