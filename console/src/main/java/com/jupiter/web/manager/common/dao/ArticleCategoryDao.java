package com.jupiter.web.manager.common.dao;

import com.jupiter.web.manager.bus.articleCategory.dto.ArticleCategoryDto;
import com.tron.common.mysql.mybatis.dao.CommonDao;
import com.jupiter.web.manager.common.entity.ArticleCategoryEntity;
import com.jupiter.web.manager.common.provider.ArticlesCategoryProvider;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleCategoryDao extends CommonDao<ArticleCategoryEntity, String> {

    @Override
    default Class<ArticleCategoryEntity> getSelfClass() {
        return ArticleCategoryEntity.class;
    }

    @SelectProvider(type = ArticlesCategoryProvider.class, method = "searchAll")
    List<ArticleCategoryEntity> searchAll(ArticleCategoryDto categoryDto);

    @Select("select * from jpt_article_category where enabled = 1 and delete_flag = 0 order by seq")
    List<ArticleCategoryEntity> getOrderList();
}
