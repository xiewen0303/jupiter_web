package com.jupiter.web.manager.common.dao;

import com.jupiter.web.manager.common.entity.LoginInfo;
import com.jupiter.web.manager.common.entity.User;
import com.tron.common.mysql.mybatis.dao.CommonDao;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * Created by zhangxiqiang on 2019/7/23.
 */
@Repository
public interface LoginInfoDao extends CommonDao<LoginInfo, String> {

    @Override
    default Class<LoginInfo> getSelfClass() {
        return LoginInfo.class;
    }
}
