package com.jupiter.web.manager.common.dao;

import com.tron.common.mysql.mybatis.dao.CommonDao;
import com.jupiter.web.manager.common.entity.User;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * Created by zhangxiqiang on 2019/7/23.
 */
@Repository
public interface UserDao extends CommonDao<User, String> {

    @Override
    default Class<User> getSelfClass() {
        return User.class;
    }

    @Select("select * from jpt_user a " +
            "left join jpt_login_session b " +
            "on a.id = b.user_id " +
            "where b.sid=#{sid} ")
    User getUserBySid(String sid);
}
