package com.jupiter.web.manager.common.dao;

import com.jupiter.web.manager.common.entity.SkyLineSendDetailLog;
import com.tron.common.mysql.mybatis.dao.CommonDao;
import org.springframework.stereotype.Repository;

/**
 * Created by zhangxiqiang on 2019/7/23.
 */
@Repository
public interface SkyLineSendDetailLogDao extends CommonDao<SkyLineSendDetailLog, String> {

    @Override
    default Class<SkyLineSendDetailLog> getSelfClass() {
        return SkyLineSendDetailLog.class;
    }
}
