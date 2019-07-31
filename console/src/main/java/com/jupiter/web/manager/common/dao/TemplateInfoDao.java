package com.jupiter.web.manager.common.dao;

import com.jupiter.web.manager.bus.article.dto.TemplateKeyInfo;
import com.jupiter.web.manager.common.entity.TemplateInfoEntity;
import com.tron.common.mysql.mybatis.dao.CommonDao;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * Created by zhangxiqiang on 2019/7/29.
 */
public interface TemplateInfoDao extends CommonDao<TemplateInfoEntity, String> {
    @Override
    default Class<TemplateInfoEntity> getSelfClass() {
        return TemplateInfoEntity.class;
    }

    @Select("SELECT a.id  article_goods_id, b.template_id FROM `jpt_article_goods_info` a\n" +
            "LEFT JOIN \n" +
            "`jpt_template_info` b\n" +
            "ON a.id = b.article_goods_id\n" +
            "WHERE a.md5_url = #{urlMD5}\n ")
    TemplateKeyInfo getTemplateKeyInfo(@Param("urlMD5") String urlMD5);
}
