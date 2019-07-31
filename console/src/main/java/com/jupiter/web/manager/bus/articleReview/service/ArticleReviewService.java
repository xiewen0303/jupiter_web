package com.jupiter.web.manager.bus.articleReview.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.jupiter.web.manager.bus.admin.store.ConfigManager;
import com.jupiter.web.manager.bus.articleReview.dto.*;
import com.jupiter.web.manager.common.base.PageInputDto;
import com.jupiter.web.manager.common.base.PageOutputDto;
import com.jupiter.web.manager.common.dao.ArticleInfoDao;
import com.jupiter.web.manager.common.dao.ArticleLikeDao;
import com.jupiter.web.manager.common.dao.ArticleReviewDao;
import com.jupiter.web.manager.common.dao.UserDao;
import com.jupiter.web.manager.common.entity.ArticleInfoEntity;
import com.jupiter.web.manager.common.entity.ArticleLikeEntity;
import com.jupiter.web.manager.common.entity.ArticleReviewEntity;
import com.jupiter.web.manager.common.entity.User;
import com.jupiter.web.manager.common.enums.LikeEnum;
import com.jupiter.web.manager.common.enums.LikeType;
import com.jupiter.web.manager.common.enums.UserLikeEnum;
import com.jupiter.web.manager.common.locale.DefaultLocale;
import com.jupiter.web.manager.utils.GenIdTools;
import com.jupiter.web.manager.utils.PageHelperUtil;
import com.tron.framework.dto.ResponseEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * 社区 -> 评论回复Service
 *
 * @author fuyuling
 * @created 2019/7/23 18:18
 */
@Slf4j
@Service
public class ArticleReviewService {

    @Resource
    private ArticleReviewDao articleReviewDao;
    @Resource
    private UserDao userDao;
    @Resource
    private ArticleLikeDao articleLikeDao;
    @Resource
    private ArticleInfoDao articleInfoDao;

    /**
     * 删除评论/回复
     */
    public ResponseEntity delete(ArticleReviewDeleteDto param, User user) {
        articleReviewDao.deleteBy(param.getReview_id(), user.getId());
        return new ResponseEntity(ResponseEntity.STATUS_OK);
    }

    /**
     * 添加评论
     */
    public ResponseEntity addReview(ArticleReviewAddDto param, User user) {
        // 验证文章有效性
        ArticleInfoEntity article = articleInfoDao.selectArticle(param.getArticle_id());
        if (article == null) {
            return new ResponseEntity(ResponseEntity.STATUS_FAIL, null, DefaultLocale.get("article.not_exists"), null);
        }
        // 插入评论数据
        ArticleReviewEntity entity = new ArticleReviewEntity();
        entity.setId(GenIdTools.getUUId());
        long now = System.currentTimeMillis();
        entity.setAddTime(now);
        entity.setAddUser(user.getId());
        entity.setArticleId(param.getArticle_id());
        entity.setContent(param.getContent());
        entity.setFromUid(user.getId());
        entity.setFromUsername(user.getNickname());
        entity.setReviewTime(now);
        entity.setUpdateTime(now);
        entity.setUpdateUser(user.getId());
        articleReviewDao.insert(entity);
        return new ResponseEntity(ResponseEntity.STATUS_OK);
    }

    /**
     * 添加回复
     */
    public ResponseEntity addReply(ArticleReplyAddDto param, User user) {
        // 验证评论有效性
        ArticleReviewEntity review = articleReviewDao.selectReview(param.getReview_id());
        if (review == null) {
            return new ResponseEntity(ResponseEntity.STATUS_FAIL, null, DefaultLocale.get("review.not_exists"), null);
        }
        // 查询文章
        ArticleInfoEntity article = articleInfoDao.selectArticle(review.getArticleId());
        // 插入回复数据
        ArticleReviewEntity entity = new ArticleReviewEntity();
        entity.setId(GenIdTools.getUUId());
        long now = System.currentTimeMillis();
        entity.setAddTime(now);
        entity.setAddUser(user.getId());
        entity.setArticleId(article.getId());
        entity.setContent(param.getContent());
        entity.setFromUid(user.getId());
        entity.setFromUsername(user.getNickname());
        entity.setReviewId(param.getReview_id());
        entity.setReviewTime(now);
        if (StringUtils.isNotBlank(param.getTo_uid()) && !StringUtils.equals(param.getTo_uid(), user.getId())) {
            User toUser = userDao.getById(param.getTo_uid());
            if (toUser != null) {
                entity.setToUid(toUser.getId());
                entity.setToUsername(toUser.getNickname());
            } else {
                if (log.isErrorEnabled()) {
                    log.error("【添加评论/回复】接收用户不存在，to_uid：{}", param.getTo_uid());
                }
            }
        }
        entity.setUpdateTime(now);
        entity.setUpdateUser(user.getId());
        articleReviewDao.insert(entity);
        return new ResponseEntity(ResponseEntity.STATUS_OK);
    }

    /**
     * 查询评论（user有可能为null，未登陆）
     */
    public ResponseEntity getReviews(ArticleReviewQueryInputDto param, User user) {
        // 查询文章是否存在
        ArticleInfoEntity article = articleInfoDao.selectArticle(param.getArticle_id());
        if (article == null) {
            return new ResponseEntity(ResponseEntity.STATUS_FAIL, null, DefaultLocale.get("article.not_exists"), null);
        }
        // 服务器设置数据的查询时间
        if(param.getPage() == null || param.getPage() == 1 || param.getTimestamp() == null) {
            param.setTimestamp(System.currentTimeMillis());
        }
        // 构建响应实体
        ArticleReviewQueryOutputDto data = new ArticleReviewQueryOutputDto();
        data.setArticle_id(article.getId());
        data.setTimestamp(param.getTimestamp());
        // 查询最新的N条评论
        int newReviewPageSize = Integer.valueOf(ConfigManager.getValue("article.review.new_review.page_size", "2"));
        List<ArticleReviewDto> newReviewList = PageHelperUtil.list(1, newReviewPageSize, () -> articleReviewDao.selectNewReviews(param));
        if (newReviewList != null && newReviewList.size() > 0) {
            int replyPageSize = Integer.valueOf(ConfigManager.getValue("article.review.reply.page_size", "10"));
            List<String> excludeIds = new ArrayList<>(newReviewList.size()); // 查询其他的评论时，需要排除掉最新的N条评论
            if(param.getPage() == null || param.getPage() == 1) {
                newReviewList.parallelStream().forEach(review -> {
                    review.setReplies(getReplies(review.getId(), param.getTimestamp(), replyPageSize, user));
                    review.setIs_like(isLike(user, review.getId()));
                    excludeIds.add(review.getId());
                });
                data.setNew_reviews(newReviewList);
            } else {
                newReviewList.parallelStream().forEach(review -> excludeIds.add(review.getId()));
            }
            // 查询其他的评论
            PageOutputDto<ArticleReviewDto> result = PageHelperUtil.page(param, () -> articleReviewDao.pageReviews(param, excludeIds));
            if (result.getData() != null && result.getData().size() > 0) {
                result.getData().parallelStream().forEach(review -> {
                    review.setReplies(getReplies(review.getId(), param.getTimestamp(), replyPageSize, user));
                    review.setIs_like(isLike(user, review.getId()));
                });
            }
            data.setReviews(result);
        }
        return new ResponseEntity(ResponseEntity.STATUS_OK, null, null, data);
    }


    /**
     * 查询文章最新的N条评论
     */
    public PageOutputDto<ArticleReviewDto> getArticleNewReviews(String articleId, Integer size, long timestamp, User user) {
        PageOutputDto<ArticleReviewDto> data = PageHelperUtil.page(1, size, () -> articleReviewDao.selectArticleNewReviews(articleId));
        int replyPageSize = Integer.valueOf(ConfigManager.getValue("article.review.reply.page_size", "10"));
        data.getData().parallelStream().forEach(review -> {
            review.setReplies(getReplies(review.getId(), timestamp, replyPageSize, user));
            review.setIs_like(isLike(user, review.getId()));
        });
        return data;
    }

    /**
     * 判断用户是否点赞评论/回复
     */
    private Integer isLike(User user, String reviewId) {
        if(user == null) {
            return UserLikeEnum.NO.getCode();
        }
        Integer like = articleLikeDao.isLike(LikeType.REVIEW.getCode(), reviewId, user.getId());
        if(like != null) {
            return UserLikeEnum.YES.getCode();
        }
        return UserLikeEnum.NO.getCode();
    }

    /**
     * 查询第一页回复
     */
    private List<ArticleReplyDto> getReplies(String reviewId, Long timestamp, Integer pageSize, User user) {
        ArticleReplyQueryInputDto param = new ArticleReplyQueryInputDto();
        param.setReview_id(reviewId);
        param.setTimestamp(timestamp);
        List<ArticleReplyDto> list = PageHelperUtil.list(1, pageSize, () -> articleReviewDao.pageReplies(param));
        list.parallelStream().forEach(reply -> reply.setIs_like(isLike(user, reply.getId())));
        return list;
    }

    /**
     * 分页查询回复（user有可能为null，未登陆）
     */
    public ResponseEntity getReplies(ArticleReplyQueryInputDto param, User user) {
        // 查询评论是否存在
        ArticleReviewEntity review = articleReviewDao.selectReview(param.getReview_id());
        if (review == null) {
            return new ResponseEntity(ResponseEntity.STATUS_FAIL, null, DefaultLocale.get("review.not_exists"), null);
        }
        // 服务器设置数据的查询时间
        if(param.getPage() == null || param.getPage() == 1 || param.getTimestamp() == null) {
            param.setTimestamp(System.currentTimeMillis());
        }
        // 查询回复
        PageOutputDto<ArticleReplyDto> result = PageHelperUtil.page(param, () -> articleReviewDao.pageReplies(param));
        result.getData().parallelStream().forEach(reply -> reply.setIs_like(isLike(user, reply.getId())));
        // 构建响应实体
        ArticleReplyQueryOutputDto data = new ArticleReplyQueryOutputDto();
        data.setReplies(result);
        data.setReview_id(review.getId());
        data.setTimestamp(param.getTimestamp());
        return new ResponseEntity(ResponseEntity.STATUS_OK, null, null, data);
    }



    /**
     * 评论/回复: 点赞/取消点赞
     */
    public ResponseEntity like(ArticleReviewLikeDto param, User user) {
        ArticleReviewEntity reviewEntity = articleReviewDao.selectReview(param.getTarget_id());
        if (reviewEntity == null) {
            return new ResponseEntity(ResponseEntity.STATUS_FAIL, null, DefaultLocale.get("article.review.not_exists"), null);
        }
        ArticleLikeEntity cond = new ArticleLikeEntity();
        cond.setTypeId(param.getTarget_id());
        cond.setType(LikeType.REVIEW.getCode());
        cond.setUid(user.getId());
        ArticleLikeEntity likeEntity = articleLikeDao.getLastOne(cond);
        long now = System.currentTimeMillis();
        if (likeEntity == null) {
            if(param.getLike() == LikeEnum.DISLIKE.getCode()) {
                return new ResponseEntity(ResponseEntity.STATUS_FAIL, null, DefaultLocale.get("params_error"), null);
            }
            likeEntity = new ArticleLikeEntity();
            likeEntity.setArticleId(reviewEntity.getArticleId());
            likeEntity.setId(GenIdTools.getUUId());
            likeEntity.setAddTime(now);
            likeEntity.setAddUser(user.getId());
            likeEntity.setLike(param.getLike());
            likeEntity.setType(LikeType.REVIEW.getCode());
            likeEntity.setTypeId(reviewEntity.getId());
            likeEntity.setUid(user.getId());
            likeEntity.setUpdateTime(now);
            likeEntity.setUpdateUser(user.getId());
            likeEntity.setOperateTime(now);
            articleLikeDao.insert(likeEntity);
        } else if(param.getLike().intValue() != likeEntity.getLike().intValue()) {
            likeEntity.setLike(param.getLike());
            likeEntity.setUpdateTime(now);
            likeEntity.setUpdateUser(user.getId());
            likeEntity.setOperateTime(now);
            articleLikeDao.update(likeEntity);
        }
        return new ResponseEntity(ResponseEntity.STATUS_OK);
    }

    /**
     * 查询所有评论（user有可能为null，未登陆）
     */
    public List<ArticleReviewDto> getReviews(ArticleReviewQueryInputDto param) {
        // 查询文章是否存在
        ArticleInfoEntity article = articleInfoDao.selectArticle(param.getArticle_id());
        if (article == null) {
          return null;
        }

        List<ArticleReviewDto> result = new ArrayList<>();

        // 查询最新的N条评论
        int newReviewPageSize = Integer.valueOf(ConfigManager.getValue("article.review.new_review.page_size", "2"));
        List<ArticleReviewDto> newReviewList = PageHelperUtil.list(1, newReviewPageSize, () -> articleReviewDao.selectNewReviews(param));

        if (newReviewList != null && newReviewList.size() > 0) {
            int replyPageSize = 200;//Integer.valueOf(ConfigManager.getValue("article.review.reply.page_size", "10"));

            List<String> excludeIds = new ArrayList<>(newReviewList.size()); // 查询其他的评论时，需要排除掉最新的N条评论
            newReviewList.parallelStream().forEach(review -> {
                List<ArticleReplyDto> articleReplyDto = getReplies(review.getId(), param.getTimestamp(), replyPageSize, null);
                review.setReplies(articleReplyDto);
                review.setIs_like(isLike(null, review.getId()));
                excludeIds.add(review.getId());
            });

            result.addAll(newReviewList);

            List<ArticleReviewDto> otherReview = articleReviewDao.pageReviews(param, excludeIds);
            if(otherReview !=  null){
                otherReview.parallelStream().forEach(review -> {
                    review.setReplies(getReplies(review.getId(), param.getTimestamp(), replyPageSize, null));
                    review.setIs_like(isLike(null, review.getId()));
                });
                result.addAll(otherReview);
            }
        }
        return result;
    }
}
