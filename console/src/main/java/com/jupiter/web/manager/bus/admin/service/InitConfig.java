package com.jupiter.web.manager.bus.admin.service;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class InitConfig  implements InitializingBean {

    @Resource
    private ConfigService configService;

    @Override
    public void afterPropertiesSet() throws Exception {
        configService.reloadConfig();
    }
}
