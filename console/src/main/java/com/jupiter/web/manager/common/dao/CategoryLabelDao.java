package com.jupiter.web.manager.common.dao;

import com.tron.common.mysql.mybatis.dao.CommonDao;
import com.jupiter.web.manager.common.entity.CategoryLabelEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryLabelDao extends CommonDao<CategoryLabelEntity, String> {

    @Override
    default Class<CategoryLabelEntity> getSelfClass() {
        return CategoryLabelEntity.class;
    }

//    @SelectProvider(type = LabelProvider.class, method = "search")
//    List<LabelEntity> search(CategoryDto dto);

//    @Select("SELECT role_name FROM role WHERE id=#{roleId}")
//    String getRoleNameByRoleId(Long roleId);

}
