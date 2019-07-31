package com.jupiter.web.manager.bus.article.service;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jupiter.web.manager.bus.admin.store.ConfigManager;
import com.jupiter.web.manager.bus.article.dto.ArticleContentDto;
import com.jupiter.web.manager.bus.article.dto.TemplateKeyInfo;
import com.jupiter.web.manager.bus.article.dto.request.*;
import com.jupiter.web.manager.bus.article.dto.response.*;
import com.jupiter.web.manager.bus.articleReview.dto.ArticleReviewDto;
import com.jupiter.web.manager.bus.articleReview.service.ArticleReviewService;
import com.jupiter.web.manager.common.base.PageOutputDto;
import com.jupiter.web.manager.common.dao.*;
import com.jupiter.web.manager.common.entity.*;
import com.jupiter.web.manager.common.enums.ArticleInfoStatus;
import com.jupiter.web.manager.common.enums.ArticleInfoType;
import com.jupiter.web.manager.common.enums.DelelteFlag;
import com.jupiter.web.manager.common.enums.LikeType;
import com.jupiter.web.manager.common.locale.DefaultLocale;
import com.jupiter.web.manager.constants.ErrorCodeConstant;
import com.jupiter.web.manager.mongo.MongoDao;
import com.jupiter.web.manager.utils.GenIdTools;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.tron.common.exception.ErrorCode;
import com.tron.common.util.HttpClientUtils;
import com.tron.common.util.MD5Utils;
import com.tron.framework.dto.ResponseEntity;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.net.URL;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.jupiter.web.manager.constants.ArticleConstant.*;

/**
 * Created by zhangxiqiang on 2019/7/18.
 */
@Slf4j
@Service
public class ArticleService {
    @Resource
    private MongoDao mongoDao;
    @Resource
    private ArticleGoodsInfoDao articleGoodsInfoDao;
    @Resource
    private ArticleGoodsRequestInfoDao articleGoodsRequestInfoDao;
    @Resource
    private ArticleInfoDao articleInfoDao;
    @Resource
    private ArticleCategoryDao articleCategoryDao;
    @Resource
    private ArticleLikeDao articleLikeDao;
    @Resource
    private ArticleReviewService articleReviewService;
    @Resource
    private UserDao userDao;
    @Resource
    private ViewTemplateDao viewTemplateDao;
    @Resource
    private TemplateInfoDao templateInfoDao;
    @Resource
    private ArticleHelperService articleHelperService;

    /**
     * @param articleContentDto 保存评论数据对象
     * @return
     */
    public String save2mongo(ArticleContentDto articleContentDto) {
        Objects.requireNonNull(articleContentDto, "内容对象不能为空");
        Objects.requireNonNull(articleContentDto.getData(), "内容数据不能为空");
        String data = articleContentDto.getData();
        ArticleContentMongoEntity reviewMongoEntity = new ArticleContentMongoEntity();
        reviewMongoEntity.setData(data);
        reviewMongoEntity = mongoDao.add(reviewMongoEntity);
        return reviewMongoEntity.getId();
    }

    /**
     * @param id 查找的评论id
     * @return
     */
    public ArticleContentMongoEntity getArticleContentById(String id) {
        return mongoDao.findById(id, ArticleContentMongoEntity.class);
    }

    /**
     * @param mongoEntity
     * @return 修改了多少条
     */
    public long updateArticleContentById(ArticleContentMongoEntity mongoEntity) {
        UpdateResult updateResult =
                mongoDao.updateById(mongoEntity);
        return updateResult.getModifiedCount();
    }

    /**
     * @return 删除了多少条
     */
    public long removeArticleContentById(String id) throws Exception {
        ArticleContentMongoEntity reviewMongoEntity = new ArticleContentMongoEntity();
        reviewMongoEntity.setId(id);
        DeleteResult deleteResult =
                mongoDao.removeById(reviewMongoEntity);
        return deleteResult.getDeletedCount();
    }

    /**
     * 根据url获取商品信息
     *
     * @param goodsCardInfoDto
     * @return
     */
    public ResponseEntity getGoodsCardLink(GoodsCardLinkRequest goodsCardInfoDto) throws Exception {
        checkUrl(goodsCardInfoDto);
        // 查找缓存,如果缓存中有直接返回
        String urlMD5 = MD5Utils.encodeByMD5(goodsCardInfoDto.getUrl());
        TemplateKeyInfo templateKeyInfo = templateInfoDao.getTemplateKeyInfo(urlMD5);
        if (templateKeyInfo != null) {
            Objects.requireNonNull(templateKeyInfo.getArticleGoodsId(),DefaultLocale.get("error.other"));
            Objects.requireNonNull(templateKeyInfo.getTemplateId(),DefaultLocale.get("error.other"));
            String viewPath =  articleHelperService.viewPath(templateKeyInfo.getTemplateId(), templateKeyInfo.getArticleGoodsId());
            GoodsCardLinkResponse goodsCardLinkResponse = new GoodsCardLinkResponse();
            goodsCardLinkResponse.setDetail_url(viewPath);
            return new ResponseEntity(ResponseEntity.STATUS_OK, goodsCardLinkResponse);
        }
        // 生成billNo,将billNo和url存储到数据库中(方便重试)
        ArticleGoodsRequestInfoEntity articleGoodsRequestInfoEntity = new ArticleGoodsRequestInfoEntity();
        articleGoodsRequestInfoEntity.setStatus(ArticleHelperService.ArticleGoodsRequestInfoStatus.STATUS_PROCESSING);
        String billNo = GenIdTools.getUUId();
        articleGoodsRequestInfoEntity.setBillNo(billNo);
        articleGoodsRequestInfoEntity.setUrl(goodsCardInfoDto.getUrl());
        articleGoodsRequestInfoEntity.setId(GenIdTools.getUUId());
        articleGoodsRequestInfoDao.insert(articleGoodsRequestInfoEntity);
        // 请求python服务并轮训检查redis的数据
        String path = null;
        try {
            path = requestGoodsCardInfo(billNo, goodsCardInfoDto.getUrl());
            articleGoodsRequestInfoEntity.setStatus(ArticleHelperService.ArticleGoodsRequestInfoStatus.STATUS_SUCCESS);
            articleGoodsRequestInfoDao.update(articleGoodsRequestInfoEntity);
        } catch (Exception ex) {
            log.error("查询商品信息失败", ex);
            articleGoodsRequestInfoEntity.setStatus(ArticleHelperService.ArticleGoodsRequestInfoStatus.STATUS_FAIL);
            articleGoodsRequestInfoDao.update(articleGoodsRequestInfoEntity);
        }
        if (path == null) {
            // 异常
            return new ResponseEntity(ResponseEntity.STATUS_FAIL);
        }
        GoodsCardLinkResponse goodsCardLinkResponse = new GoodsCardLinkResponse();
        goodsCardLinkResponse.setDetail_url(path);
        // 返回结果
        return new ResponseEntity(ResponseEntity.STATUS_OK, goodsCardLinkResponse);
    }

    /**
     * 检查url
     *
     * @param goodsCardInfoDto
     */
    private String checkUrl(GoodsCardLinkRequest goodsCardInfoDto) {
        try {
            new URL(goodsCardInfoDto.getUrl());
        } catch (Exception ex) {
            log.error("url : {} 不是一个标准的url", goodsCardInfoDto.getUrl());
            return null;
        }
        // 判断网站地址是不是指定的网站
        String regex = ConfigManager.getValue(ARTICLE_GOODS_URL_REGEX_KEY);
        if (StringUtils.isBlank(regex)) {
            log.error("系统配置code:{}必须存在", ARTICLE_GOODS_URL_REGEX_KEY);
        }
        Pattern pattern = Pattern.compile(regex);
        if (!pattern.matcher(goodsCardInfoDto.getUrl()).matches()) {
            return null;
        }
        return goodsCardInfoDto.getUrl();
    }

    /**
     * 请求python获取数据,返回可用的url
     *
     * @param billNo
     * @param url
     */
    private String requestGoodsCardInfo(String billNo, String url) throws Exception {
        String goodsCardInfoUrl = ConfigManager.getValue(GOODS_CARD_INFO_REQUEST_URL_KEY);
        Map<String, String> params = new HashMap<>();
        params.put("bill_no", billNo);
        params.put("url", url);
        // 一般这里是异步的
        String response = HttpClientUtils.doPost(goodsCardInfoUrl, params);
        ResponseEntity responseEntity = JSON.parseObject(response, ResponseEntity.class);
        if (!responseEntity.isOK()) {
            throw new RuntimeException(DefaultLocale.get("error.other"));
        }
        GoodsCardInfoResultRequest goodsCardInfoResultRequest = JSON.parseObject(responseEntity.getData().toString(), GoodsCardInfoResultRequest.class);
        return articleHelperService.getGoodsCardUrl(goodsCardInfoResultRequest);
    }




    public ResponseEntity update(ArticleInfoRequest articleInfoDto, User user) {
        if (StringUtils.isNotBlank(articleInfoDto.getTitle()) && articleInfoDto.getTitle().length() > 30) {
            Objects.requireNonNull(articleInfoDto, DefaultLocale.get("params_error"));
        }
        if (StringUtils.isNotBlank(articleInfoDto.getContent()) && articleInfoDto.getContent().length() < 30 && articleInfoDto.getContent().length() > 30000) {
            Objects.requireNonNull(articleInfoDto, DefaultLocale.get("params_error"));
        }
        if (articleInfoDto.getArticle_id() == null) {
            // 新建
            ArticleInfoEntity articleInfoEntity = new ArticleInfoEntity();
            setArticleInfoEntity(articleInfoEntity, articleInfoDto, user);
            articleInfoDao.insert(articleInfoEntity);
        } else {
            ArticleInfoEntity articleInfoEntityLastOne = checkArticleExist(articleInfoDto.getArticle_id());
            // 检查当前用户是不是第一次操作的人
            if (!articleInfoEntityLastOne.getUserId().equals(user.getId())) {
                log.error("文章:{}的创建者id：{}和编辑者id：{}不一致", JSON.toJSONString(articleInfoEntityLastOne), articleInfoEntityLastOne.getUserId(), user.getId());
                return new ResponseEntity(ResponseEntity.STATUS_FAIL, ErrorCode.DEFAULT_FAIL_CODE, DefaultLocale.get("permission.error"), null);
            }
            // 审核中、已发布的文章只能查看，不能编辑
            if (articleInfoEntityLastOne.getStatus().equals(ArticleInfoStatus.AUDITING.getCode())
                    || articleInfoEntityLastOne.getStatus().equals(ArticleInfoStatus.SUCCESS.getCode())) {
                log.error("审核中、已发布的文章只能查看，不能编辑");
                return new ResponseEntity(ResponseEntity.STATUS_FAIL, ErrorCode.DEFAULT_FAIL_CODE, DefaultLocale.get("article.status.error"), null);
            }
            setArticleInfoEntity(articleInfoEntityLastOne, articleInfoDto, user);
            articleInfoDao.updateWithNull(articleInfoEntityLastOne);
        }

        return new ResponseEntity(ResponseEntity.STATUS_OK);
    }

    private void setArticleInfoEntity(ArticleInfoEntity articleInfoEntity, ArticleInfoRequest articleInfoDto, User user) {
        articleInfoEntity.setCategoryId(articleInfoDto.getCategory_id());
        articleInfoEntity.setId(GenIdTools.getUUId());
        articleInfoEntity.setHeadPic(articleInfoDto.getHead_pic());
        articleInfoEntity.setTitle(articleInfoDto.getTitle());
        articleInfoEntity.setUserId(user.getId());
        articleInfoEntity.setAddUser(user.getId());
        if (articleInfoDto.getType().equals(ArticleInfoType.SAVE.getCode())) {
            articleInfoEntity.setStatus(ArticleInfoStatus.DRAFT.getCode());
        } else if (articleInfoDto.getType().equals(ArticleInfoType.PUBLISH.getCode())) {
            articleInfoEntity.setStatus(ArticleInfoStatus.AUDITING.getCode());
        } else {
            log.error("暂不支持操作类型:{}", articleInfoDto.getType());
            throw new RuntimeException(DefaultLocale.get("error.other"));
        }

        if (StringUtils.isNotBlank(articleInfoDto.getContent())) {
            if (articleInfoEntity.getContentRefId() == null) {
                ArticleContentDto articleContentDto = new ArticleContentDto();
                articleContentDto.setData(articleInfoDto.getContent());
                String id = save2mongo(articleContentDto);
                articleInfoEntity.setContentRefId(id);
            } else {
                // 如果表中已经存在内容引用
                ArticleContentMongoEntity articleContentMongoEntity = new ArticleContentMongoEntity();
                articleContentMongoEntity.setData(articleInfoDto.getContent());
                // 更新mongo数据
                updateArticleContentById(articleContentMongoEntity);
            }
        }
        // 非官方文章
        articleInfoEntity.setIsOffer(0);
    }

    public ResponseEntity get(String articleId, User user) {
        long now = System.currentTimeMillis();
        // 根据id查文章
        ArticleInfoEntity articleInfoLastOne = checkArticleExist(articleId);
        ArticleInfoResponse articleInfoResponse = new ArticleInfoResponse();
        // 添加发布用户
        User u = new User();
        u.setId(articleInfoLastOne.getUserId());
        User userInfo = userDao.getOne(u);
        if (userInfo == null) {
            log.error("用户Id:{}未找到对应的用户信息");
            return new ResponseEntity(ResponseEntity.STATUS_FAIL, ErrorCode.DEFAULT_FAIL_CODE, DefaultLocale.get("error.other"), null);
        }
        articleInfoResponse.setName(userInfo.getNickname() == null ? userInfo.getMobile() : userInfo.getNickname());
        articleInfoResponse.setArticle_id(articleId);
        if (StringUtils.isBlank(articleInfoLastOne.getCategoryId())) {
            log.error("文章:{}的类目信息不存在", articleInfoLastOne);
            return new ResponseEntity(ResponseEntity.STATUS_FAIL, ErrorCode.DEFAULT_FAIL_CODE, DefaultLocale.get("error.other"), null);
        }
        articleInfoResponse.setCategory_id(articleInfoLastOne.getCategoryId());
        if (StringUtils.isNotBlank(articleInfoLastOne.getContentRefId())) {
            articleInfoResponse.setContent(getArticleContentById(articleInfoLastOne.getContentRefId()).getData());
        }
        articleInfoResponse.setPublish_time(articleInfoLastOne.getPublishTime());
        articleInfoResponse.setHead_pic(articleInfoLastOne.getHeadPic());
        articleInfoResponse.setTitle(articleInfoLastOne.getTitle());
        ArticleCategoryEntity articleCategoryEntity = new ArticleCategoryEntity();
        articleCategoryEntity.setId(articleInfoLastOne.getCategoryId());
        ArticleCategoryEntity articleCategoryLastOne = articleCategoryDao.getLastOne(articleCategoryEntity);
        if (articleCategoryLastOne == null) {
            log.error("文章类目id:{}找不到对应的类目信息", articleInfoLastOne.getCategoryId());
            return new ResponseEntity(ResponseEntity.STATUS_FAIL, ErrorCode.DEFAULT_FAIL_CODE, DefaultLocale.get("error.other"), null);
        }
        articleInfoResponse.setCategory_name(articleCategoryLastOne.getName());

        // 查询部分评论数
        int INIT_SIZE = 3;
        PageOutputDto<ArticleReviewDto> articleNewReviewList = articleReviewService.getArticleNewReviews(articleId, INIT_SIZE, now, user);
        ArticleInfoResponse.ReviewInfo reviewInfo = new ArticleInfoResponse.ReviewInfo();
        articleInfoResponse.setReview_info(reviewInfo);
        if (CollectionUtils.isEmpty(articleNewReviewList.getData())) {
            reviewInfo.setReview_count(0);
            reviewInfo.setDetail(Collections.emptyList());
        } else {
            List<ArticleInfoResponse.ReviewDetail> reviewDetailList = articleNewReviewList.getData().stream().map(ele -> {
                ArticleInfoResponse.ReviewDetail reviewDetail = new ArticleInfoResponse.ReviewDetail();
                reviewDetail.setAvatar_file_url(ele.getAvatar_file_url());
                reviewDetail.setContent(ele.getContent());
                reviewDetail.setFrom_username(ele.getFrom_username());
                reviewDetail.setLike_num(ele.getLike_num());
                reviewDetail.setReview_time(ele.getReview_time());
                reviewDetail.setIs_like(ele.getIs_like());
                reviewDetail.setId(ele.getId());
                return reviewDetail;
            }).collect(Collectors.toList());
            reviewInfo.setDetail(reviewDetailList);
            reviewInfo.setReview_count(articleNewReviewList.getRecordsTotal());
        }
        return new ResponseEntity(ResponseEntity.STATUS_OK, articleInfoResponse);

    }

    /**
     * @param articleId
     * @param user
     * @return
     */
    public ResponseEntity remove(String articleId, User user) throws Exception {
        ArticleInfoEntity articleInfoEntity = new ArticleInfoEntity();
        articleInfoEntity.setId(articleId);
        articleInfoEntity.setDeleteFlag(DelelteFlag.AVAILABLE.getCode());
        ArticleInfoEntity articleInfoLastOne = articleInfoDao.getLastOne(articleInfoEntity);
        if (!articleInfoLastOne.getUserId().equals(user.getId())) {
            log.error("文章编写人用户id:{}和准备删除人用户id:{}不一致", articleInfoLastOne.getUserId(), user.getId());
            return new ResponseEntity(ResponseEntity.STATUS_FAIL, ErrorCode.DEFAULT_FAIL_CODE, DefaultLocale.get("permission.error"), null);
        }
        // 草稿、未通过的文章可以编辑、删除
        boolean canDelete = articleInfoLastOne.getStatus().equals(ArticleInfoStatus.DRAFT.getCode()) ||
                articleInfoLastOne.getStatus().equals(ArticleInfoStatus.FAIL.getCode());
        if (!canDelete) {
            log.error("【草稿、未通过的文章可以编辑、删除】,但是当前文章状态为:{}", articleInfoLastOne.getStatus());
            return new ResponseEntity(ResponseEntity.STATUS_FAIL, ErrorCode.DEFAULT_FAIL_CODE, DefaultLocale.get("article.status.error"), null);
        }
        // 标记删除
        articleInfoLastOne.setDeleteFlag(DelelteFlag.DELETE.getCode());
        articleInfoLastOne.setUpdateUser(user.getId());
        articleInfoDao.update(articleInfoLastOne);
        return new ResponseEntity(ResponseEntity.STATUS_OK);
    }

    public ResponseEntity getCategory() {
        List<ArticleCategoryEntity> articleCategoryEntityList = articleCategoryDao.getOrderList();
        List<ArticleCategoryResponse> responseList = new ArrayList<>();
        responseList.add(new ArticleCategoryResponse("ALL", null));
        for (ArticleCategoryEntity articleCategoryEntity : articleCategoryEntityList) {
            ArticleCategoryResponse articleCategoryResponse = new ArticleCategoryResponse();
            articleCategoryResponse.setCategory_id(articleCategoryEntity.getId());
            articleCategoryResponse.setName(articleCategoryEntity.getName());
            responseList.add(articleCategoryResponse);
        }
        return new ResponseEntity(ResponseEntity.STATUS_OK, responseList);
    }

    /**
     * 点赞
     *
     * @param articleLikeRequest
     * @param user
     * @return
     */
    public ResponseEntity like(ArticleLikeRequest articleLikeRequest, User user) {
        // 检查文章是不是存在
        checkArticleExist(articleLikeRequest.getArticle_id());
        // 点赞/取消点赞（如果已经点赞/取消点赞了，就不操作）
        ArticleLikeEntity likeEntity = new ArticleLikeEntity();
        likeEntity.setTypeId(articleLikeRequest.getArticle_id());
        likeEntity.setType(LikeType.ARTICLE.getCode());
        likeEntity.setUid(user.getId());
        ArticleLikeEntity likeLastOne = articleLikeDao.getLastOne(likeEntity);
        if (likeLastOne == null) {
            // 当前用户没有点赞/取消点赞记录,只能进行点赞
            if (articleLikeRequest.getLike() == 0) {
                log.error("当前用户没有点赞/取消点赞记录,只能进行点赞");
                return new ResponseEntity(ResponseEntity.STATUS_FAIL, ErrorCodeConstant.LIKE_STATUS_EXCEPTION, DefaultLocale.get("like.status.error"), null);
            } else {
                // 添加一条新的记录
                likeEntity.setLike(articleLikeRequest.getLike());
                likeEntity.setId(GenIdTools.getUUId());
                likeEntity.setAddUser(user.getId());
                likeEntity.setUpdateUser(user.getId());
                articleLikeDao.insert(likeEntity);
            }
        } else {
            // 已经有点赞记录
            if (likeLastOne.getLike().equals(articleLikeRequest.getLike())) {
                log.error("点赞状态和数据库中状态一致，操作失败");
                return new ResponseEntity(ResponseEntity.STATUS_FAIL, ErrorCodeConstant.LIKE_EXIST, DefaultLocale.get("like.status.error"), null);
            }
            likeEntity.setLike(articleLikeRequest.getLike());
            likeEntity.setUpdateUser(user.getId());
            articleLikeDao.update(likeEntity);
        }

        return new ResponseEntity(ResponseEntity.STATUS_OK);
    }

    /**
     * 根据文章id检查文章是否存在，如果存在就返回该对象
     *
     * @param articleId
     * @return
     */
    private ArticleInfoEntity checkArticleExist(String articleId) {
        Objects.requireNonNull(articleId, DefaultLocale.get(""));
        ArticleInfoEntity articleInfoEntity = new ArticleInfoEntity();
        articleInfoEntity.setId(articleId);
        articleInfoEntity.setDeleteFlag(DelelteFlag.AVAILABLE.getCode());
        ArticleInfoEntity articleInfoLastOne = articleInfoDao.getLastOne(articleInfoEntity);
        if (articleInfoLastOne == null) {
            log.error("文章id:{}不存在", articleId);
            throw new RuntimeException(DefaultLocale.get("article.not.found"));
        }
        return articleInfoLastOne;
    }

    public ResponseEntity getAll(ArticleListRequest articleListRequest, User user) {
        // 修改请求时间
        if (articleListRequest.getTimestamp() == null) {
            // 取服务器时间
            articleListRequest.setTimestamp(System.currentTimeMillis());
        }
        ArticleListResponse articleListResponse = new ArticleListResponse();
        List<ArticleListResponse.ArticleListDetail> detailArrayList = null;
        String userId = null;
        if (user == null) {
            userId = null;
        } else {
            userId = user.getId();
        }
        ListRange listRange = getListRange(userId, articleListRequest.getPage(), articleListRequest.getRows(), articleListRequest.getCategory_id(), articleListRequest.getTimestamp());
        List<ArticleListResponse.ArticleListDetail> articleListResponseList = articleInfoDao.getArticleListResponse(userId, articleListRequest.getCategory_id(), articleListRequest.getTimestamp(), listRange.getOffset(), listRange.getLimit());
        List<ArticleListResponse.ArticleListDetail> curOrderDataList = listRange.getArticleListDetailList();

        // 校验is_like字段
        for (ArticleListResponse.ArticleListDetail articleListDetail : articleListResponseList) {
            if (articleListDetail.getIs_like() > 1) {
                log.error("文章id：{}点赞条目多于1条", articleListDetail.getArticle_id());
                return new ResponseEntity(ResponseEntity.STATUS_FAIL, ErrorCode.DEFAULT_FAIL_CODE, DefaultLocale.get("error.other"), null);
            }
        }

        for (ArticleListResponse.ArticleListDetail articleListDetail : curOrderDataList) {
            if (articleListDetail.getIs_like() > 1) {
                log.error("文章id：{}点赞条目多于1条", articleListDetail.getArticle_id());
                return new ResponseEntity(ResponseEntity.STATUS_FAIL, ErrorCode.DEFAULT_FAIL_CODE, DefaultLocale.get("error.other"), null);
            }
        }

        if (curOrderDataList.size() == 0) {
            detailArrayList = articleListResponseList;
        } else {
            int minIndexPos = (articleListRequest.getPage() - 1) * articleListRequest.getRows();

            // 不存在order字段集合的最大下标位置
            int maxIndexPos = minIndexPos + articleListResponseList.size();
            int minOrderPos = curOrderDataList.get(0).getOrder();
            // order位置从1开始
            if (maxIndexPos < minOrderPos - 1) {
                // 不用转换为LinkedList
                articleListResponseList.addAll(curOrderDataList);
                detailArrayList = articleListResponseList;
            } else {
                List<ArticleListResponse.ArticleListDetail> list = new LinkedList(articleListResponseList);
                ;
                for (ArticleListResponse.ArticleListDetail detail : curOrderDataList) {
                    if (detail.getOrder() - 1 <= maxIndexPos) {
                        // 存在交叉位置
                        list.add(detail.getOrder() - 1 - minIndexPos, detail);
                    } else {
                        list.add(detail);
                    }
                }
                detailArrayList = list;
            }
        }
        articleListResponse.setDetail(detailArrayList);
        articleListResponse.setTimestamp(articleListRequest.getTimestamp());


        return new ResponseEntity(ResponseEntity.STATUS_OK, articleListResponse);
    }


    private ListRange getListRange(String userId, Integer page, Integer rows, String categoryId, Long timestamp) {
        int curMinOffset = (page - 1) * rows;
        int curMaxOffset = page * rows;
        // 查询order的分布
        List<ArticleListResponse.ArticleListDetail> validOrderList = articleInfoDao.getValidOrder(userId, curMaxOffset, timestamp, categoryId);
        // 前面已经被order数据占用了多少个位置
        int preOrderCount = 0;
        // 当前含有order数据集合
        List<ArticleListResponse.ArticleListDetail> curOrderDataList = new ArrayList<>();
        for (ArticleListResponse.ArticleListDetail detail : validOrderList) {
            if (detail.getOrder() < curMinOffset) {
                preOrderCount++;
            } else if (detail.getOrder() >= curMinOffset && detail.getOrder() < curMinOffset + rows) {
                curOrderDataList.add(detail);
            }
        }
        ListRange listRange = new ListRange();
        listRange.setOffset(curMinOffset - preOrderCount);
        listRange.setLimit(rows - curOrderDataList.size());
        listRange.setArticleListDetailList(curOrderDataList);
        return listRange;
    }

    public ResponseEntity getPublishInfo(PublishInfoRequest publishInfoRequest, User user) {
        if (publishInfoRequest.getTimestamp() == null) {
            publishInfoRequest.setTimestamp(System.currentTimeMillis());
        }
        PublishInfoResponse publishInfoResponse = new PublishInfoResponse();
        PageHelper.startPage(publishInfoRequest.getPage(), publishInfoRequest.getRows());
        List<PublishInfoResponse.PublishInfoDetail> detail = articleInfoDao.getPublishInfo(user.getId());
        PageInfo<PublishInfoResponse.PublishInfoDetail> pageInfo = new PageInfo(detail);
        publishInfoResponse.setDetail(pageInfo.getList());
        publishInfoResponse.setTimestamp(publishInfoRequest.getTimestamp());
        return new ResponseEntity(ResponseEntity.STATUS_OK, publishInfoResponse);
    }


    public GoodsCardInfoViewResponse getGoodsCardInfo(GoodsCardInfoRequest goodsCardInfoRequest) {
        Objects.requireNonNull(goodsCardInfoRequest, DefaultLocale.get("error.other"));
        Objects.requireNonNull(goodsCardInfoRequest.getTemplate_id(), DefaultLocale.get("error.other"));
        Objects.requireNonNull(goodsCardInfoRequest.getArticle_goods_id(), DefaultLocale.get("error.other"));
        String article_goods_id = goodsCardInfoRequest.getArticle_goods_id();
        ArticleGoodsInfoEntity articleGoodsInfoEntity = new ArticleGoodsInfoEntity();
        articleGoodsInfoEntity.setId(article_goods_id);
        ArticleGoodsInfoEntity articleGoodsInfoLastOne = articleGoodsInfoDao.getLastOne(articleGoodsInfoEntity);
        if (articleGoodsInfoLastOne == null) {
            return null;
        }
        GoodsCardInfoViewResponse goodsCardInfoViewResponse = new GoodsCardInfoViewResponse();
        GoodsCardInfoResponse goodsCardInfoResponse = new GoodsCardInfoResponse();
        goodsCardInfoResponse.setOrigin(articleGoodsInfoLastOne.getOrigin());
        goodsCardInfoResponse.setDiscount(articleGoodsInfoLastOne.getDiscount());
        goodsCardInfoResponse.setIcon(articleGoodsInfoLastOne.getIcon());
        goodsCardInfoResponse.setOrigin_price(articleGoodsInfoLastOne.getOriginPrice());
        goodsCardInfoResponse.setUrl(articleGoodsInfoLastOne.getUrl());
        goodsCardInfoResponse.setPrice(articleGoodsInfoLastOne.getPrice());
        goodsCardInfoResponse.setPrice_unit(articleGoodsInfoLastOne.getPriceUnit());
        goodsCardInfoResponse.setTitle(articleGoodsInfoLastOne.getTitle());
        goodsCardInfoResponse.setScore(articleGoodsInfoLastOne.getScore());
        goodsCardInfoViewResponse.setResponse(goodsCardInfoResponse);


        // 模板路径,不能查找viewTemplate中有效的（可能存在将active的view变成不被激活的）
        ViewTemplateEntity viewTemplateEntity = new ViewTemplateEntity();
        viewTemplateEntity.setId(goodsCardInfoRequest.getTemplate_id());
        ViewTemplateEntity viewTemplateLastOne = viewTemplateDao.getLastOne(viewTemplateEntity);
        goodsCardInfoViewResponse.setPath(viewTemplateLastOne.getPath());
        return goodsCardInfoViewResponse;
    }

    /**
     *
     */
    @Data
    private class ListRange {
        private Integer offset;
        private Integer limit;
        private List<ArticleListResponse.ArticleListDetail> articleListDetailList; // 当前含有order数据集合
    }




}
