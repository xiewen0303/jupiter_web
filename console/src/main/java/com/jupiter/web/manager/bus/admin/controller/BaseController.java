package com.jupiter.web.manager.bus.admin.controller;

import com.jupiter.web.manager.common.entity.Admin;
import com.jupiter.web.manager.utils.SessionContextUtils;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * 控制器基类 所有控制器必须继承该类
 *
 * @author KD
 */
public abstract class BaseController {

    @Autowired
    protected SessionContextUtils sessionContextUtils;

    protected Long getLoginAdminId() {
        Admin adminInfo = (Admin) sessionContextUtils.getContextRequest().getSession().getAttribute("login_admin");
        if (null != adminInfo) {
            return adminInfo.getId();
        }
        return null;
    }
}
