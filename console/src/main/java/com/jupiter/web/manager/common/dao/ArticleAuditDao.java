package com.jupiter.web.manager.common.dao;

import com.jupiter.web.manager.common.entity.ArticleAuditEntity;
import com.tron.common.mysql.mybatis.dao.CommonDao;

public interface ArticleAuditDao extends CommonDao<ArticleAuditEntity, String> {
    @Override
    default Class<ArticleAuditEntity> getSelfClass() {
        return ArticleAuditEntity.class;
    }

}
