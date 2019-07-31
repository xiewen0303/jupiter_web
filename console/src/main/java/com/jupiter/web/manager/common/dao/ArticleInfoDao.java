package com.jupiter.web.manager.common.dao;

import com.jupiter.web.manager.bus.article.dto.ArticleManagerDto;
import com.jupiter.web.manager.bus.article.dto.response.ArticleListResponse;
import com.jupiter.web.manager.bus.article.dto.response.PublishInfoResponse;
import com.tron.common.mysql.mybatis.dao.CommonDao;
import com.jupiter.web.manager.common.entity.ArticleInfoEntity;
import com.jupiter.web.manager.common.provider.ArticleInfoProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

/**
 * Created by zhangxiqiang on 2019/7/23.
 */
public interface ArticleInfoDao extends CommonDao<ArticleInfoEntity, String> {
    @Override
    default Class<ArticleInfoEntity> getSelfClass() {
        return ArticleInfoEntity.class;
    }

    @Results({
            @Result(column="article_id",property="article_id"),
            @Result(column="head_pic",property="head_pic"),
            @Result(column="like_count",property="like_count"),
            @Result(column="review_count",property="review_count"),
            @Result(column="is_like",property="is_like"),
    })
    @Select("<script>"+
            "SELECT a.id article_id,a.title title,a.`head_pic`,b.like_count,c.review_count,d.mobile name,d.avatar_file_url icon,IFNULL(b.is_like,0) is_like FROM  jpt_article_info a " +
            "LEFT JOIN " +
            "(" +
            "SELECT type_id id,COUNT(1) like_count ,sum(CASE  WHEN  uid = #{userId} THEN 1 ELSE 0 END) is_like FROM jpt_article_like WHERE `type` = 1 AND `_like` = 1 GROUP BY type_id " +
            ") b " +
            "ON a.id = b.id " +
            "LEFT JOIN " +
            "(" +
            "SELECT  article_id,COUNT(1) review_count FROM jpt_article_review WHERE delete_flag = 0 AND review_id IS NULL and  check_status = 1 " +
            ") c " +
            "ON a.id = c.article_id " +
            "LEFT JOIN jpt_user d " +
            "on d.id = a.user_id " +
            "WHERE a.`delete_flag` = 0 and a.`_status` = 4 and a.`_order` is null " +
            "<if test=\"categoryId!=null\"> " +
            "and a.category_id=#{categoryId} " +
            "</if> " +
            "<if test=\"timestamp!=null\"> " +
            "and a.add_time  &lt;= #{timestamp} " +
            "</if> " +
            "order by a.publish_time desc " +
            "limit #{offset},#{limit}"+
            "</script>"
    )
    List<ArticleListResponse.ArticleListDetail> getArticleListResponse(@Param("userId") String userId,@Param("categoryId") String categoryId,@Param("timestamp") Long timestamp,@Param("offset") Integer offset ,@Param("limit") Integer limit);



    @Results({
            @Result(column="article_id",property="article_id"),
            @Result(column="head_pic",property="head_pic"),
            @Result(column="like_count",property="like_count"),
            @Result(column="review_count",property="review_count"),
            @Result(column="is_like",property="is_like"),
    })
    @Select(
            "<script>"+
                    "SELECT a.id article_id,a.`_order` , a.title title,a.`head_pic`,b.like_count,c.review_count,d.mobile name,d.avatar_file_url icon,IFNULL(b.is_like,0) is_like FROM  jpt_article_info a " +
                    "LEFT JOIN " +
                    "(" +
                    "SELECT type_id id,COUNT(1) like_count ,sum(CASE  WHEN  uid =#{userId} THEN 1 ELSE 0 END) is_like   FROM jpt_article_like WHERE `type` = 1 AND `_like` = 1 GROUP BY type_id " +
                    ") b " +
                    "ON a.id = b.id " +
                    "LEFT JOIN" +
                    "(" +
                    "SELECT  article_id,COUNT(1) review_count FROM jpt_article_review WHERE delete_flag = 0 AND review_id IS NULL and  check_status = 1" +
                    ") c " +
                    "ON a.id = c.article_id " +
                    "LEFT JOIN jpt_user d " +
                    "on d.id = a.user_id " +
                    "WHERE a.`delete_flag` = 0 and a.`_status` = 4 " +
                    "<if test='categoryId!=null'> " +
                    "and a.category_id = #{categoryId} " +
                    "</if> " +
                    "<if test='timestamp!=null'> " +
                    "and a.add_time &lt;= #{timestamp} " +
                    "</if> " +
                    "and `_order` is not null and `_order` &lt; #{maxOffset} " +
                    "order by `_order` "+
            "</script>"
    )
    List<ArticleListResponse.ArticleListDetail> getValidOrder(@Param("userId") String userId, @Param("maxOffset") Integer maxOffset ,@Param("timestamp") Long timestamp,@Param("categoryId") String categoryId);

    @Results({
            @Result(column="article_id",property="article_id"),
            @Result(column="article_status",property="article_status"),
            @Result(column="head_pic",property="head_pic"),
    })
    @Select("SELECT a.id article_id,a.status article_status,a.title title,a.`head_pic`,b.mobile name,b.avatar_file_url icon FROM  jpt_article_info a " +
            "LEFT JOIN jpt_user b " +
            "on b.id = a.user_id " +
            "WHERE a.`delete_flag` = 0 " +
            "order by a.publish_time desc " )
    List<PublishInfoResponse.PublishInfoDetail> getPublishInfo(@Param("userId") String userId);

    @SelectProvider(type = ArticleInfoProvider.class, method = "articleInfoPages")
    List<ArticleInfoEntity> getArticleInfoPages(ArticleManagerDto req);

    /**
     * 查询未被删除的有效的文章
     */
    @Select("select * from jpt_article_info where id=#{id} and delete_flag=0")
    ArticleInfoEntity selectArticle(@Param("id") String id);


    @Select("select COUNT(1) review_count  FROM jpt_article_review WHERE  article_id=#{articleId} and  delete_flag = 0 AND review_id IS NULL AND check_status = 1")
    Integer getCommentsCount(String articleId);


    @Select("SELECT SUM(_like) FROM jpt_article_like WHERE type_id = #{articleId} and type= 1")
    Integer getlikeCount(String articleId);
}
