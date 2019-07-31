package com.jupiter.web.manager.common.dao;

import com.jupiter.web.manager.common.entity.SkyLineSendLog;
import com.tron.common.mysql.mybatis.dao.CommonDao;
import org.springframework.stereotype.Repository;

/**
 * Created by zhangxiqiang on 2019/7/23.
 */
@Repository
public interface SkyLineSendLogDao extends CommonDao<SkyLineSendLog, String> {

    @Override
    default Class<SkyLineSendLog> getSelfClass() {
        return SkyLineSendLog.class;
    }
}
