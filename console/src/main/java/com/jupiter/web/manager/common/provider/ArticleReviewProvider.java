package com.jupiter.web.manager.common.provider;

import com.jupiter.web.manager.bus.articleReview.dto.ArticleReplyQueryInputDto;
import com.jupiter.web.manager.bus.articleReview.dto.ArticleReviewQueryInputDto;
import com.jupiter.web.manager.common.enums.LikeType;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;

/**
 * @author fuyuling
 * @created 2019/7/24 19:44
 */
public class ArticleReviewProvider {

    private String getLikeNumSql() {
        return String.format("(select count(1) from jpt_article_like b where b.type_id=a.id and b.type=%s and b._like=1) as like_num", LikeType.REVIEW.getCode());
    }

    public String selectNewReviews(ArticleReviewQueryInputDto param) {
        SQL sql = new SQL();
        sql.SELECT("a.id", "a.content", "u.avatar_file_url", "a.from_username", "a.from_uid", "a.review_time", getLikeNumSql());
        sql.FROM("jpt_article_review a").LEFT_OUTER_JOIN("jpt_user u ON a.from_uid=u.id");
        sql.WHERE("a.article_id=#{article_id}", "a.delete_flag=0", "a.check_status=1", "a.review_id is null", "a.review_time<=#{timestamp}");
        sql.ORDER_BY("a.review_time DESC");
        return sql.toString();
    }

    public String pageReviews(@Param("param") ArticleReviewQueryInputDto param, @Param("excludeIds") List<String> excludeIds) {
        SQL sql = new SQL();
        sql.SELECT("a.id", "a.content", "u.avatar_file_url", "a.from_username", "a.from_uid", "a.review_time", getLikeNumSql());
        sql.FROM("jpt_article_review a").LEFT_OUTER_JOIN("jpt_user u ON a.from_uid=u.id");
        sql.WHERE("a.article_id=#{param.article_id}", "a.delete_flag=0", "a.check_status=1", "a.review_id is null", "a.review_time<=#{param.timestamp}");
        if(excludeIds != null && excludeIds.size() > 0) {
            StringBuilder notInSql = new StringBuilder("a.id not in(");
            for(String excludeId: excludeIds) {
                notInSql.append("'").append(excludeId).append("',");
            }
            notInSql.replace(notInSql.length() - 1, notInSql.length(), ")");
            sql.WHERE(notInSql.toString());
        }
        sql.ORDER_BY("like_num DESC", "a.review_time DESC");
        return sql.toString();
    }

    public String pageReplies(ArticleReplyQueryInputDto param) {
        SQL sql = new SQL();
        sql.SELECT("id", "content", "from_username", "to_username", "from_uid", "to_uid", "review_time", getLikeNumSql());
        sql.FROM("jpt_article_review a");
        sql.WHERE("review_id=#{review_id}", "delete_flag=0", "check_status=1", "review_time<=#{timestamp}");
        sql.ORDER_BY("review_time ASC");
        return sql.toString();
    }

    public String selectArticleNewReviews(String articleId) {
        SQL sql = new SQL();
        sql.SELECT("a.id", "a.content", "u.avatar_file_url", "a.from_username", "a.from_uid", "a.review_time", getLikeNumSql());
        sql.FROM("jpt_article_review a").LEFT_OUTER_JOIN("jpt_user u ON a.from_uid=u.id");
        sql.WHERE("a.article_id=#{article_id}", "a.delete_flag=0", "a.check_status=1", "a.review_id is null");
        sql.ORDER_BY("a.review_time DESC");
        return sql.toString();
    }

}
