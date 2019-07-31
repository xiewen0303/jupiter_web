package com.jupiter.web.manager.common.dao;

import com.tron.common.mysql.mybatis.dao.CommonDao;
import com.jupiter.web.manager.common.entity.LoginSession;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * Created by zhangxiqiang on 2019/7/23.
 */
@Repository
public interface LoginSessionDao extends CommonDao<LoginSession, String> {


    @Override
    default Class<LoginSession> getSelfClass() {
        return LoginSession.class;
    }

    @Delete("delete from jpt_login_session where user_id=#{userId} and add_channel=#{addChannel}")
    void deleteByParam(String userId, String addChannel);

    @Delete("delete from jpt_login_session where sid=#{sid}")
    void deleteBySid(String sid);
}
