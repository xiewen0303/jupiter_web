package com.jupiter.web.manager.common.dao;

import com.tron.common.mysql.mybatis.dao.CommonDao;
import com.jupiter.web.manager.common.entity.CategoryEntity;
import com.jupiter.web.manager.common.entity.CategoryMappingEntity;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryMappingDao extends CommonDao<CategoryMappingEntity, String> {

    @Override
    default Class<CategoryMappingEntity> getSelfClass() {
        return CategoryMappingEntity.class;
    }

    //删除相关的数据
    @Update("update jpt_goods_category_mapping set delete_flag = 1 where code=#{code}")
    int deleteCategoryMapping(String code);
}
