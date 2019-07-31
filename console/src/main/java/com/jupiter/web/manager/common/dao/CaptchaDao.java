package com.jupiter.web.manager.common.dao;

import com.jupiter.web.manager.common.entity.Captcha;
import com.tron.common.mysql.mybatis.dao.CommonDao;
import org.springframework.stereotype.Repository;

/**
 * Created by zhangxiqiang on 2019/7/23.
 */
@Repository
public interface CaptchaDao extends CommonDao<Captcha, String> {

    @Override
    default Class<Captcha> getSelfClass() {
        return Captcha.class;
    }

}
