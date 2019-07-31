package com.jupiter.web.manager.common.dao;

import com.jupiter.web.manager.bus.articleReview.dto.ArticleReplyDto;
import com.jupiter.web.manager.bus.articleReview.dto.ArticleReplyQueryInputDto;
import com.jupiter.web.manager.bus.articleReview.dto.ArticleReviewDto;
import com.jupiter.web.manager.bus.articleReview.dto.ArticleReviewQueryInputDto;
import com.tron.common.mysql.mybatis.dao.CommonDao;
import com.jupiter.web.manager.common.entity.ArticleReviewEntity;
import com.jupiter.web.manager.common.provider.ArticleReviewProvider;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;

/**
 * Created by zhangxiqiang on 2019/7/23.
 */
public interface ArticleReviewDao extends CommonDao<ArticleReviewEntity, String> {

    @Override
    default Class<ArticleReviewEntity> getSelfClass() {
        return ArticleReviewEntity.class;
    }

    /**
     * 根据ID逻辑删除评论/回复
     */
    @Update("update jpt_article_review set delete_flag=1,update_time=UNIX_TIMESTAMP()*1000,update_user=#{updateUser} where id=#{id} and delete_flag=0")
    void deleteBy(@Param("id") String id, @Param("updateUser") String updateUser);

    /**
     * 根据ID查询未被删除的且未下架的评论/回复
     */
    @Select("select * from jpt_article_review where id=#{id} and delete_flag=0 and check_status=1")
    ArticleReviewEntity selectReview(String id);

    /**
     * 查询最新的评论
     */
    @Results({
            @Result(column = "from_username", property = "from_username", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(column = "from_uid", property = "from_uid", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(column = "avatar_file_url", property = "avatar_file_url", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(column = "like_num", property = "like_num", javaType = Long.class, jdbcType = JdbcType.INTEGER),
            @Result(column = "review_time", property = "review_time", javaType = Long.class, jdbcType = JdbcType.INTEGER)
    })
    @SelectProvider(type = ArticleReviewProvider.class, method = "selectNewReviews")
    List<ArticleReviewDto> selectNewReviews(ArticleReviewQueryInputDto param);

    /**
     * 查询其他评论
     */
    @Results({
            @Result(column = "from_username", property = "from_username", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(column = "from_uid", property = "from_uid", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(column = "avatar_file_url", property = "avatar_file_url", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(column = "like_num", property = "like_num", javaType = Long.class, jdbcType = JdbcType.INTEGER),
            @Result(column = "review_time", property = "review_time", javaType = Long.class, jdbcType = JdbcType.INTEGER)
    })
    @SelectProvider(type = ArticleReviewProvider.class, method = "pageReviews")
    List<ArticleReviewDto> pageReviews(@Param("param") ArticleReviewQueryInputDto param, @Param("excludeIds") List<String> excludeIds);

    /**
     * 查询回复
     */
    @Results({
            @Result(column = "from_username", property = "from_username", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(column = "to_username", property = "to_username", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(column = "from_uid", property = "from_uid", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(column = "to_uid", property = "to_uid", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(column = "like_num", property = "like_num", javaType = Long.class, jdbcType = JdbcType.INTEGER),
            @Result(column = "review_time", property = "review_time", javaType = Long.class, jdbcType = JdbcType.INTEGER)
    })
    @SelectProvider(type = ArticleReviewProvider.class, method = "pageReplies")
    List<ArticleReplyDto> pageReplies(ArticleReplyQueryInputDto param);

    @Results({
            @Result(column = "from_username", property = "from_username", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(column = "from_uid", property = "from_uid", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(column = "avatar_file_url", property = "avatar_file_url", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(column = "like_num", property = "like_num", javaType = Long.class, jdbcType = JdbcType.INTEGER),
            @Result(column = "review_time", property = "review_time", javaType = Long.class, jdbcType = JdbcType.INTEGER)
    })
    @SelectProvider(type = ArticleReviewProvider.class, method = "selectArticleNewReviews")
    List<ArticleReviewDto> selectArticleNewReviews(String articleId);
}
