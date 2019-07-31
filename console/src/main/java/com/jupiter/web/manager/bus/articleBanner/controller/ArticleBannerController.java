package com.jupiter.web.manager.bus.articleBanner.controller;

import com.github.pagehelper.PageInfo;
import com.jupiter.web.manager.bus.admin.dto.CommonResponse;
import com.jupiter.web.manager.bus.banner.dto.BannerReq;
import com.jupiter.web.manager.bus.banner.service.BannerService;
import com.jupiter.web.manager.common.entity.BannerEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("articleBanner")
public class ArticleBannerController {

    @Resource
    private BannerService bannerService;

    @RequestMapping(value = "/addBanner")
    public String addBanner(){
        return "articleBanner/addBanner";
    }

    @ResponseBody
    @RequestMapping(value = "/submitBanner")
    public CommonResponse submitBanner(BannerReq bannerReq) throws IOException {
         return bannerService.submitBanner(bannerReq);
    }

    @RequestMapping(value = "/search")
    public String search(){
        return "articleBanner/bannerList";
    }

    @ResponseBody
    @RequestMapping(value = "/updateBanner")
    public CommonResponse updateBanner(String bannerId,int type){
        return bannerService.updateArticleBanner(bannerId,type);
    }

    @RequestMapping(value = "/modifyBanner")
    public String modifyBanner(Model model,String bannerId) {
        if(StringUtils.isEmpty(bannerId)) {
            return "articleBanner/bannerModify";
        }

        BannerEntity oldBannerEntity = bannerService.getLastArticleBanner(bannerId);
        model.addAttribute("bannerEntity",oldBannerEntity);
        return "articleBanner/modifyBanner";
    }

    @ResponseBody
    @RequestMapping(value = "/getBannerInfoPages")
    public CommonResponse<PageInfo<BannerEntity>> getBannerInfoPages(BannerReq bannerReq) {

        CommonResponse<PageInfo<BannerEntity>> result = new CommonResponse<>();
        bannerReq.setLocation("article_top");
        List<BannerEntity> list = bannerService.getArticleBannerList(bannerReq);
        PageInfo<BannerEntity> pageInfo = new PageInfo<>(list);

        result.setModule(pageInfo);
        result.setCode("200");
        result.setMsg("查询成功");
        return result;
    }
}
