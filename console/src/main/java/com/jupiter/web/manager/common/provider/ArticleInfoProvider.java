package com.jupiter.web.manager.common.provider;

import com.jupiter.web.manager.bus.article.dto.ArticleManagerDto;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.util.StringUtils;

public class ArticleInfoProvider {

    public static String articleInfoPages(ArticleManagerDto req) {

        SQL querySql = new SQL();
        querySql.SELECT("jpt_article_info.*,jpt_article_category.name as category_name,jpt_user.mobile as mobile,jpt_user.nickname as nickname");
        querySql.FROM("jpt_article_info","jpt_article_category","jpt_user");
        querySql.WHERE("jpt_article_info.category_id=jpt_article_category.id");
        querySql.WHERE("jpt_user.id=jpt_article_info.user_id");
        querySql.WHERE("jpt_article_info.delete_flag = 0 ");

        if(!StringUtils.isEmpty(req.getTitle())){
            querySql.WHERE("jpt_article_info.title like   CONCAT('%',#{title},'%')");
        }

        if(null != req.getBeginTime()){
            querySql.WHERE("jpt_article_info.add_time >= #{beginTime}");
        }

        if(null != req.getEndTime()){
            querySql.WHERE("jpt_article_info.add_time <= #{endTime}");
        }

        if(null != req.getStatus()){
            querySql.WHERE("jpt_article_info._status = #{status}");
        }

        if(null != req.getCategoryId()){
            querySql.WHERE("jpt_article_info.category_id = #{categoryId}");
        }

        if(null != req.getIsOffer()){
            querySql.WHERE("jpt_article_info.is_offer = #{isOffer}");
        }

        querySql.ORDER_BY("jpt_article_info.update_time desc");
        return querySql.toString();
    }
}
