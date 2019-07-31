package com.jupiter.web.manager.bus.admin.service;


import com.jupiter.web.manager.bus.admin.store.ConfigManager;
import com.jupiter.web.manager.common.dao.ConfigDao;
import com.jupiter.web.manager.common.entity.ConfigEntity;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ConfigService{

    @Resource
    private ConfigDao configDao;

    public void reloadConfig() throws Exception {
        List<ConfigEntity> configs = configDao.getAll();
        ConfigManager.init(configs);
    }
}
