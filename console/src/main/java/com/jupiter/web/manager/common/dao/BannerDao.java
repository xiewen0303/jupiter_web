package com.jupiter.web.manager.common.dao;

import com.jupiter.web.manager.bus.banner.dto.BannerReq;
import com.tron.common.mysql.mybatis.dao.CommonDao;
import com.jupiter.web.manager.common.entity.BannerEntity;
import com.jupiter.web.manager.common.provider.BannerProvider;
import org.apache.ibatis.annotations.SelectProvider;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BannerDao extends CommonDao<BannerEntity, String> {

    @Override
    default Class<BannerEntity> getSelfClass() {
        return BannerEntity.class;
    }

    @SelectProvider(type = BannerProvider.class, method = "searchAll")
    List<BannerEntity> searchAll(BannerReq req);

}
