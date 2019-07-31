package com.jupiter.web.manager.bus.articleBanner.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ArticleBannerService {
//
//    @Resource
//    private OSSClient ossClient;
//
//    @Resource
//    private ArticleBannerDao articleBannerDao;
//
//    public void writeFile2OSS(MultipartFile multipartFile, String objectName) throws IOException {
//        String bucketName = ConfigManager.getValue("oss.common.bucketname");
//        ossClient.putObject(bucketName, objectName, multipartFile.getInputStream());
//    }
//
//
//    public CommonResponse submitBanner(ArticleBannerReq bannerReq){
//        CommonResponse result = new CommonResponse();
//        MultipartFile multipartFile = bannerReq.getBannerFile();
//        String fileNameOld = multipartFile.getOriginalFilename();
//        String bannerId = bannerReq.getBannerId();
//        if(StringUtils.isEmpty(bannerId) && StringUtils.isEmpty(fileNameOld)) {
//            result.setCode("401");
//            result.setMsg("图片不能为空");
//            return result;
//        }
//
//        String src = null;
//        ArticleBannerEntity bannerEntity = new ArticleBannerEntity();
//        if(!StringUtils.isEmpty(fileNameOld)){
//
//            String prefix = fileNameOld.substring(fileNameOld.lastIndexOf(".")+1);
//            src = GenIdTools.getUUId()+"."+prefix;
//            String dateStr = DateUtil.formatDate(System.currentTimeMillis(),"yyyyMMdd");
//            String objectName = "articleBanner/"+dateStr+"/"+src;
//            try {
//                this.writeFile2OSS(multipartFile,objectName);
//            } catch (IOException e) {
//                log.error("上传社区Banner图片失败！");
//                e.printStackTrace();
//            }
//            src = ConfigManager.getValue("common-outside-url-prefixurl")+objectName;
//        }
//
//        if(StringUtils.isEmpty(bannerReq.getBannerId())){
//            bannerEntity.setId(GenIdTools.getUUId());
//            bannerEntity.setAddTime(System.currentTimeMillis());
//            bannerEntity.setUpdateTime(System.currentTimeMillis());
//            bannerEntity.setBannerName(bannerReq.getBannerName());
//            bannerEntity.setHref(bannerReq.getHref());
//            bannerEntity.setSrc(src);
//            bannerEntity.setNeedLogin(0);
//            bannerEntity.setType("href");
//            bannerEntity.setDeleteFlag(0);
//            bannerEntity.setEnabled(0);
//            bannerEntity.setSeq(bannerReq.getSeq());
//            bannerEntity.setTarget(bannerReq.getTarget());
//            bannerEntity.setLocation("homepage_top");
//            articleBannerDao.insert(bannerEntity);
//        }else{
//            bannerEntity.setId(bannerReq.getBannerId());
//            bannerEntity = articleBannerDao.getLastOne(bannerEntity);
//            if(bannerEntity == null){
//                log.error("数据库不存在，id={}",bannerReq.getBannerId());
//                result.setCode("401");
//                result.setMsg("数据异常，没找到需要修改的数据！");
//                return result;
//            }
//
//            bannerEntity.setBannerName(bannerReq.getBannerName());
//            if(!StringUtils.isEmpty(src)) {
//                bannerEntity.setSrc(src);
//            }
//            bannerEntity.setHref(bannerReq.getHref());
//            bannerEntity.setSeq(bannerReq.getSeq());
//            bannerEntity.setTarget(bannerReq.getTarget());
//            articleBannerDao.update(bannerEntity);
//        }
//
//        result.setCode("200");
//        result.setMsg("success");
//        return result;
//    }
//
//    public List<BannerEntity> getArticleBannerList(ArticleBannerReq bannerReq) {
//        if (!StringUtils.isEmpty(bannerReq.getPage()) && !StringUtils.isEmpty(bannerReq.getRows())) {
//            PageHelper.startPage(bannerReq.getPage(), bannerReq.getRows());
//        }
//        return  articleBannerDao.searchAll(bannerReq);
//    }
//
//
//    public CommonResponse updateArticleBanner(String bannerId,int type){
//        CommonResponse  result = new CommonResponse();
//        if(StringUtils.isEmpty(bannerId)){
//            result.setCode("201");
//            result.setMsg("唯一id不能为空！");
//            return result;
//        }
//        ArticleBannerEntity params = new ArticleBannerEntity();
//        params.setId(bannerId);
//        ArticleBannerEntity oldBanner = articleBannerDao.getLastOne(params);
//        if(oldBanner == null){
//            result.setCode("201");
//            result.setMsg("操作失败,数据库没有该数据!");
//            return result;
//        }
//
//        switch (type){
//            case 1: //删除
//                params.setDeleteFlag(1);
//                break;
//            case 2: //下架
//                params.setEnabled(0);
//                break;
//            case 3: //上架
//                params.setEnabled(1);
//                break;
//            default:
//                log.error("更新banner类型不支持,type={}",type);
//                result.setCode("201");
//                result.setMsg("操作失败,更新banner类型不支持！");
//                return result;
//        }
//
//        articleBannerDao.update(params);
//
//        result.setCode("200");
//        result.setMsg("操作成功！");
//        return result;
//    }
//
//    public ArticleBannerEntity getLastArticleBanner(String bannerId) {
//        ArticleBannerEntity params = new ArticleBannerEntity();
//        params.setId(bannerId);
//        return articleBannerDao.getLastOne(params);
//    }
//
//    /**
//     * 查询手机可以显示的社区banner
//     * @return
//     */
//    public List<ArticleBannerListOutputDto> getAllArticleBanner() {
//        return  articleBannerDao.searchAllShow();
//    }
}
