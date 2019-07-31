package com.jupiter.web.manager.common.dao;

import com.tron.common.mysql.mybatis.dao.CommonDao;
import com.jupiter.web.manager.common.entity.ArticleGoodsInfoEntity;

/**
 * Created by zhangxiqiang on 2019/7/23.
 */
public interface ArticleGoodsInfoDao extends CommonDao<ArticleGoodsInfoEntity, String> {

    @Override
    default Class<ArticleGoodsInfoEntity> getSelfClass() {
        return ArticleGoodsInfoEntity.class;
    }


}
