package com.jupiter.web.manager.bus.article.service;

import com.jupiter.web.manager.bus.admin.store.ConfigManager;
import com.jupiter.web.manager.bus.article.dto.request.GoodsCardInfoResultRequest;
import com.jupiter.web.manager.common.dao.ArticleGoodsInfoDao;
import com.jupiter.web.manager.common.dao.ArticleGoodsRequestInfoDao;
import com.jupiter.web.manager.common.dao.TemplateInfoDao;
import com.jupiter.web.manager.common.dao.ViewTemplateDao;
import com.jupiter.web.manager.common.entity.ArticleGoodsInfoEntity;
import com.jupiter.web.manager.common.entity.ArticleGoodsRequestInfoEntity;
import com.jupiter.web.manager.common.entity.TemplateInfoEntity;
import com.jupiter.web.manager.common.entity.ViewTemplateEntity;
import com.jupiter.web.manager.utils.GenIdTools;
import com.tron.common.util.MD5Utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Map;

import static com.jupiter.web.manager.constants.ArticleConstant.ARTICLE_GOODS_CARD_INFO_TEMPLATE_CODE;
import static com.jupiter.web.manager.constants.ArticleConstant.GOODS_CARD_INFO_URL_KEY;

/**
 * Created by zhangxiqiang on 2019/7/30.
 */
@Slf4j
@Service
public class ArticleHelperService {
    @Resource
    private ArticleGoodsRequestInfoDao articleGoodsRequestInfoDao;
    @Resource
    private ArticleGoodsInfoDao articleGoodsInfoDao;
    @Resource
    private ViewTemplateService viewTemplateService;
    @Resource
    private TemplateInfoDao templateInfoDao;

    /**
     * 根据商品卡片返回url
     *
     * @param goodsCardInfoResultRequest
     * @return
     * @throws Exception
     */
    @Transactional
    public String getGoodsCardUrl(GoodsCardInfoResultRequest goodsCardInfoResultRequest) throws Exception {
        String billNo = goodsCardInfoResultRequest.getBillNo();
        ArticleGoodsRequestInfoEntity articleGoodsRequestInfoEntity = new ArticleGoodsRequestInfoEntity();
        articleGoodsRequestInfoEntity.setBillNo(billNo);
        ArticleGoodsRequestInfoEntity articleGoodsRequestInfoLastOne = articleGoodsRequestInfoDao.getLastOne(articleGoodsRequestInfoEntity);
        if (articleGoodsRequestInfoLastOne == null) {
            log.error("根据流水号:{}在商品信息请求表中不存在", billNo);

        } else {
            Integer status = articleGoodsRequestInfoLastOne.getStatus();
            if (status == ArticleGoodsRequestInfoStatus.STATUS_SUCCESS && StringUtils.isNotBlank(articleGoodsRequestInfoEntity.getArticleGoodsInfoId())) {
                log.error("流水号:{}对象的商品信息已经处理完成了", billNo);
            }
        }
        // 保存数据
        ArticleGoodsInfoEntity articleGoodsInfoEntity = new ArticleGoodsInfoEntity();
        String urlMD5 = MD5Utils.encodeByMD5(articleGoodsRequestInfoLastOne.getUrl());
        articleGoodsInfoEntity.setMd5Url(urlMD5);
        articleGoodsInfoEntity.setDiscount(goodsCardInfoResultRequest.getDiscount());
        articleGoodsInfoEntity.setIcon(goodsCardInfoResultRequest.getIcon());
        String goodsInfoId = GenIdTools.getUUId();
        articleGoodsInfoEntity.setId(goodsInfoId);
        articleGoodsInfoEntity.setOriginPrice(goodsCardInfoResultRequest.getOrigin_price());
        articleGoodsInfoEntity.setPrice(goodsCardInfoResultRequest.getPrice());
        articleGoodsInfoEntity.setPriceUnit(goodsCardInfoResultRequest.getPrice_unit());
        articleGoodsInfoEntity.setScore(goodsCardInfoResultRequest.getScore());
        articleGoodsInfoEntity.setTitle(goodsCardInfoResultRequest.getTitle());
        articleGoodsInfoEntity.setUrl(articleGoodsRequestInfoLastOne.getUrl());
        articleGoodsInfoEntity.setOrigin(goodsCardInfoResultRequest.getOrigin());
        articleGoodsInfoDao.insert(articleGoodsInfoEntity);


        // 更新数据
        articleGoodsRequestInfoLastOne.setStatus(ArticleGoodsRequestInfoStatus.STATUS_SUCCESS);
        articleGoodsRequestInfoLastOne.setArticleGoodsInfoId(goodsInfoId);
        articleGoodsRequestInfoDao.update(articleGoodsRequestInfoLastOne);

        // 请求路径
        return getNewViewPath(articleGoodsInfoEntity.getId());
    }


    private String getNewViewPath(String articleGoodsId) {
        Map<String, ViewTemplateEntity> validMap = viewTemplateService.checkViewTemplate();
        ViewTemplateEntity viewTemplateEntity = validMap.get(ARTICLE_GOODS_CARD_INFO_TEMPLATE_CODE);
        if (viewTemplateEntity == null) {
            log.error("code:{}没有找到对应的模板", ARTICLE_GOODS_CARD_INFO_TEMPLATE_CODE);
        }
        String templateId = viewTemplateEntity.getId();

        TemplateInfoEntity templateInfoEntity = new TemplateInfoEntity();
        templateInfoEntity.setArticleGoodsId(articleGoodsId);
        templateInfoEntity.setTemplateId(templateId);
        templateInfoEntity.setId(GenIdTools.getUUId());
        // 保存日志
        templateInfoDao.insert(templateInfoEntity);
        return viewPath(templateId, articleGoodsId);
    }


    public String viewPath(String templateId, String articleGoodsId) {
        String cardInfoUrlPrefix = ConfigManager.getValue(GOODS_CARD_INFO_URL_KEY);
        return cardInfoUrlPrefix  + "?template_id=" + templateId + "&article_goods_id=" + articleGoodsId;
    }


    public interface ArticleGoodsRequestInfoStatus {
        int STATUS_PROCESSING = 1;
        int STATUS_SUCCESS = 2;
        int STATUS_FAIL = 3;
    }

}
