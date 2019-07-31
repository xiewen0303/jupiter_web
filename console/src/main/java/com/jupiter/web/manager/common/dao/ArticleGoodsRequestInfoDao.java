package com.jupiter.web.manager.common.dao;

import com.tron.common.mysql.mybatis.dao.CommonDao;
import com.jupiter.web.manager.common.entity.ArticleGoodsRequestInfoEntity;

/**
 * Created by zhangxiqiang on 2019/7/23.
 */
public interface ArticleGoodsRequestInfoDao extends CommonDao<ArticleGoodsRequestInfoEntity, String> {
    @Override
    default Class<ArticleGoodsRequestInfoEntity> getSelfClass() {
        return ArticleGoodsRequestInfoEntity.class;
    }

}
