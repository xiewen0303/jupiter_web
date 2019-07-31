package com.jupiter.web.manager.common.dao;

import com.tron.common.mysql.mybatis.dao.CommonDao;
import com.jupiter.web.manager.common.entity.ArticleLikeEntity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by zhangxiqiang on 2019/7/23.
 */
public interface ArticleLikeDao extends CommonDao<ArticleLikeEntity, String> {

    @Override
    default Class<ArticleLikeEntity> getSelfClass() {
        return ArticleLikeEntity.class;
    }

    @Select("select sum(_like) from jpt_article_like where type_id=#{typeId} and type=#{type}")
    Long selectLikeNum(@Param("type") Integer type, @Param("typeId") String typeId);

    @Select("select 1 from jpt_article_like where type_id=#{typeId} and type=#{type} and uid=#{uid} limit 1")
    Integer isLike(@Param("type") Integer type, @Param("typeId") String typeId, @Param("uid") String uid);

}
