package com.jupiter.web.manager.common.dao;

import com.jupiter.web.manager.bus.category.dto.CategoryDto;
import com.tron.common.mysql.mybatis.dao.CommonDao;
import com.jupiter.web.manager.common.entity.CategoryEntity;
import com.jupiter.web.manager.common.entity.ConfigEntity;
import com.jupiter.web.manager.common.entity.SouceSiteEntity;
import com.jupiter.web.manager.common.provider.CategoryProvider;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConfigDao extends CommonDao<ConfigEntity, String> {

    @Override
    default Class<ConfigEntity> getSelfClass() {
        return ConfigEntity.class;
    }
}
