package com.jupiter.web.manager.common.dao;

import com.jupiter.web.manager.bus.category.dto.CategoryDto;
import com.tron.common.mysql.mybatis.dao.CommonDao;
import com.jupiter.web.manager.common.entity.LabelEntity;
import com.jupiter.web.manager.common.provider.LabelProvider;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LabelDao extends CommonDao<LabelEntity, String> {

    @Override
    default Class<LabelEntity> getSelfClass() {
        return LabelEntity.class;
    }

    @SelectProvider(type = LabelProvider.class, method = "search")
    List<LabelEntity> search(CategoryDto dto);

    @Select("SELECT role_name FROM role WHERE id=#{roleId}")
    String getRoleNameByRoleId(Long roleId);

}
