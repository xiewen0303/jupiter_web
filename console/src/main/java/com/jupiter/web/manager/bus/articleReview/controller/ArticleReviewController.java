package com.jupiter.web.manager.bus.articleReview.controller;

import com.jupiter.web.aop.LoginUser;
import com.jupiter.web.manager.bus.articleReview.dto.*;
import com.jupiter.web.manager.bus.articleReview.service.ArticleReviewService;
import com.jupiter.web.manager.common.entity.User;
import com.tron.framework.dto.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 社区 -> 评论回复接口
 *
 * @author fuyuling
 * @created 2019/7/23 18:01
 */
@RestController
@RequestMapping("/jupiter/api/web/article-review")
public class ArticleReviewController {

    @Resource
    private ArticleReviewService articleReviewService;

    /**
     * 提交评论
     */
    @ResponseBody
    @RequestMapping("/submit-review")
    public ResponseEntity submitReview(@Validated ArticleReviewAddDto param, @LoginUser User user) {
        return articleReviewService.addReview(param, user);
    }

    /**
     * 提交回复
     */
    @ResponseBody
    @RequestMapping("/submit-reply")
    public ResponseEntity submitReply(@Validated ArticleReplyAddDto param, @LoginUser User user) {
        return articleReviewService.addReply(param, user);
    }

    /**
     * 删除评论/回复
     */
    @ResponseBody
    @RequestMapping("/delete")
    public ResponseEntity delete(@Validated ArticleReviewDeleteDto param, @LoginUser User user) {
        return articleReviewService.delete(param, user);
    }

    /**
     * 分页 查询评论
     */
    @ResponseBody
    @RequestMapping("/reviews")
    public ResponseEntity getReviews(@Validated ArticleReviewQueryInputDto param, @LoginUser(require = false) User user) {
        return articleReviewService.getReviews(param, user);
    }

    /**
     * 分页 查询回复
     */
    @ResponseBody
    @RequestMapping("/replies")
    public ResponseEntity getReplies(@Validated ArticleReplyQueryInputDto param, @LoginUser(require = false) User user) {
        return articleReviewService.getReplies(param, user);
    }

    /**
     * 点赞 / 取消点赞
     */
    @ResponseBody
    @RequestMapping("/like")
    public ResponseEntity like(@Validated ArticleReviewLikeDto param, @LoginUser User user) {
        return articleReviewService.like(param, user);
    }

}
