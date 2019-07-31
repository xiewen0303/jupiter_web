package com.jupiter.web.manager.common.dao;

import com.jupiter.web.manager.common.entity.ViewTemplateEntity;
import com.tron.common.mysql.mybatis.dao.CommonDao;

/**
 * Created by zhangxiqiang on 2019/7/29.
 */
public interface ViewTemplateDao extends CommonDao<ViewTemplateEntity, String> {

    @Override
    default Class<ViewTemplateEntity> getSelfClass() {
        return ViewTemplateEntity.class;
    }
}
