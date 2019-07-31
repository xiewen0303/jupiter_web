package com.jupiter.web.manager.common.provider;

import com.jupiter.web.manager.bus.banner.dto.BannerReq;
import org.springframework.util.StringUtils;

public class BannerProvider {

    public String searchAll(BannerReq req) {
        StringBuffer result = new StringBuffer("select * from jpt_banner where delete_flag = 0 ");
        if (!StringUtils.isEmpty(req.getBannerName())){
            result.append(" and banner_name like CONCAT('%',#{bannerName},'%')");
        }

        if (!StringUtils.isEmpty(req.getLocation())){
            result.append(" and location=#{location}");
        }
        result.append(" ORDER BY seq asc ");
        return result.toString();
    }
}
