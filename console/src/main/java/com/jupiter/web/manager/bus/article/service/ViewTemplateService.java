package com.jupiter.web.manager.bus.article.service;

import com.jupiter.web.manager.common.dao.ViewTemplateDao;
import com.jupiter.web.manager.common.entity.ViewTemplateEntity;
import com.jupiter.web.manager.common.locale.DefaultLocale;
import com.jupiter.web.manager.constants.ArticleConstant;
import com.jupiter.web.manager.redis.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangxiqiang on 2019/7/29.
 */
@Slf4j
@Service
public class ViewTemplateService {

    @Resource
    private ViewTemplateDao viewTemplateDao;
    @Resource
    private RedisService redisService;

    /**
     * @return
     */
    public Map<String, ViewTemplateEntity> checkViewTemplate() {
        Map<String, ViewTemplateEntity> ret = redisService.get(ArticleConstant.VIEW_TEMPLATE_REDIS_KEY);
        if (ret != null) {
            return ret;
        }
        List<ViewTemplateEntity> list = viewTemplateDao.getAll();
        Map<String, ViewTemplateEntity> validMap = new HashMap();
        // 判断是否存在，同一code但是有多个active=1的数据
        List<String> codeList = new ArrayList<>();
        for (ViewTemplateEntity viewTemplateEntity : list) {
            String code = viewTemplateEntity.getCode();
            Integer active = viewTemplateEntity.getActice();
            if (active != null && active == 1) {
                if (codeList.contains(code)) {
                    log.error("ViewTemplate中可用的code：{}重复", code);
                    throw new RuntimeException(DefaultLocale.get("error.other"));
                } else {
                    codeList.add(code);
                    validMap.put(code, viewTemplateEntity);
                }
            }
        }
        if (validMap.size() == 0) {
            log.error("ViewTemplate中没有可用的code");
            throw new RuntimeException(DefaultLocale.get("error.other"));
        }
        redisService.set(ArticleConstant.VIEW_TEMPLATE_REDIS_KEY, validMap);
        return validMap;
    }
}
