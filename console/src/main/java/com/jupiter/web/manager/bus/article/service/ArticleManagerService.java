package com.jupiter.web.manager.bus.article.service;

import com.aliyun.oss.OSSClient;
import com.jupiter.web.manager.bus.admin.dto.CommonResponse;
import com.jupiter.web.manager.bus.admin.store.ConfigManager;
import com.jupiter.web.manager.bus.article.dto.ArticleManagerDto;
import com.jupiter.web.manager.bus.article.dto.request.ArticleInfoReq;
import com.jupiter.web.manager.common.dao.*;
import com.jupiter.web.manager.common.entity.ArticleAuditEntity;
import com.jupiter.web.manager.common.entity.ArticleCategoryEntity;
import com.jupiter.web.manager.common.entity.ArticleInfoEntity;
import com.jupiter.web.manager.common.entity.BannerEntity;
import com.jupiter.web.manager.mongo.MongoDao;
import com.jupiter.web.manager.redis.RedisService;
import com.jupiter.web.manager.utils.DateUtil;
import com.jupiter.web.manager.utils.GenIdTools;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

/**
 * Created by zhangxiqiang on 2019/7/18.
 */
@Slf4j
@Service
public class ArticleManagerService {

    @Resource
    private MongoDao mongoDao;
    @Resource
    private ArticleGoodsInfoDao articleGoodsInfoDao;
    @Resource
    private ArticleGoodsRequestInfoDao articleGoodsRequestInfoDao;
    @Resource
    private RedisService redisService;
    @Resource
    private ArticleInfoDao articleInfoDao;
    @Resource
    private ArticleCategoryDao articleCategoryDao;
    @Resource
    private ArticleAuditDao articleAuditDao;
    @Resource
    private OSSClient ossClient;


    public List<ArticleInfoEntity> submitBanner(ArticleManagerDto articleManagerDto) {
        return articleInfoDao.getArticleInfoPages(articleManagerDto);
    }

    /**
     * 获取文章信息
     * @param id
     * @return
     */
    public ArticleInfoEntity loadLastArticleInfo(String id) {
        ArticleInfoEntity articleInfoEntity = new ArticleInfoEntity();
        articleInfoEntity.setId(id);
        return articleInfoDao.getLastOne(articleInfoEntity);
    }

    public int rejectAricleInfo(ArticleManagerDto articleManagerDto) {

        ArticleAuditEntity audit = new ArticleAuditEntity();
        audit.setArticleId(articleManagerDto.getArticleId());
        audit.setRegistMsg(articleManagerDto.getRejectMsg());
        audit.setStatus(articleManagerDto.getStatus());
        audit.setAddTime(System.currentTimeMillis());
        audit.setUpdateTime(System.currentTimeMillis());
        audit.setId(GenIdTools.getUUId());
        int flag = articleAuditDao.insert(audit);

        if(flag > 0){
            ArticleInfoEntity articleInfoEntity = new ArticleInfoEntity();
            articleInfoEntity.setId(articleManagerDto.getArticleId());
            articleInfoEntity.setStatus(articleManagerDto.getStatus());
            flag = articleInfoDao.update(articleInfoEntity);
        }
        return flag;
    }


    public CommonResponse updateOfficiaArticle(String articleId, int type){
        CommonResponse  result = new CommonResponse();
        if(StringUtils.isEmpty(articleId)){
            result.setCode("201");
            result.setMsg("唯一id不能为空！");
            return result;
        }
        ArticleInfoEntity params = new ArticleInfoEntity();
        params.setId(articleId);
        ArticleInfoEntity oldBanner = articleInfoDao.getLastOne(params);
        if(oldBanner == null){
            result.setCode("201");
            result.setMsg("操作失败,数据库没有该数据!");
            return result;
        }

        switch (type){
            case 5: //删除
                params.setDeleteFlag(1);
                break;
            case 2: //下架
                params.setStatus(2);
                break;
            case 4: //上架
                params.setStatus(4);
                break;
            default:
                log.error("更新文章类型不支持,type={}",type);
                result.setCode("201");
                result.setMsg("操作失败,更新文章类型不支持！");
                return result;
        }

        articleInfoDao.update(params);

        result.setCode("200");
        result.setMsg("操作成功！");
        return result;
    }

    public int getCommentsCount(String id) {
        Integer result = articleInfoDao.getCommentsCount(id);
        return result == null ? 0:result;
    }

    public int getLikeCount(String id) {
        Integer result = articleInfoDao.getlikeCount(id);
        return result == null ? 0:result;
    }

    /**
     * 编辑
     * @param req
     * @return
     */
    public CommonResponse modifyArticleInfo(ArticleInfoReq req) {
        CommonResponse result = new CommonResponse();

        ArticleInfoEntity entity = loadLastArticleInfo(req.getId());
        if (null == entity) {
            log.error("articleInfo数据不存在，id={}", req.getId());
            result.setCode("201");
            result.setMsg("编辑的文章不存在，id=" + req.getId());
            return result;
        }

        MultipartFile multipartFile = req.getHeadFile();
        String fileNameOld = multipartFile.getOriginalFilename();

        String src = null;
        if (!StringUtils.isEmpty(fileNameOld)) {
            String prefix = fileNameOld.substring(fileNameOld.lastIndexOf(".") + 1);
            src = GenIdTools.getUUId() + "." + prefix;
            String dateStr = DateUtil.formatDate(System.currentTimeMillis(), "yyyyMMdd");
            String objectName = "head/" + dateStr + "/" + src;
            try {
                this.writeFile2OSS(multipartFile, objectName);
            } catch (IOException e) {
                log.error("上传头图失败,id={}", req.getId(),e);
                result.setCode("202");
                result.setMsg("修改文章头图失败，id=" + req.getId());
                return result;
            }
            src = ConfigManager.getValue("common-outside-url-prefixurl") + objectName;
        }

        if(!StringUtils.isEmpty(src)){
            entity.setHeadPic(src);
        }

        if(entity.getIsOffer() == 1){
            entity.setOrder(req.getOrder());
        }

        entity.setCategoryId(req.getCategoryId());
        entity.setTitle(req.getTitle());

        int flag = articleInfoDao.update(entity);
        if(flag <=0) {
            log.error("文章数据库更新失败,id={}", req.getId());
            result.setCode("203");
            result.setMsg("文章数据库更新失败，id=" + req.getId());
            return result;
        }
        result.setCode("200");
        result.setMsg("保存成功！");
        return result;
    }

    public void writeFile2OSS(MultipartFile multipartFile, String objectName) throws IOException {
        String bucketName = ConfigManager.getValue("oss.common.bucketname");
        ossClient.putObject(bucketName, objectName, multipartFile.getInputStream());
    }
}
