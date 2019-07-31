package com.jupiter.web.manager.bus.admin.store;


import com.jupiter.web.manager.common.entity.ConfigEntity;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConfigManager {

    volatile static Map<String, String> result = new ConcurrentHashMap<>();

    public static void init(List<ConfigEntity> configs) {
        if (configs == null) {
            return;
        }
        Map<String, String> oldResult = result;
        for (ConfigEntity configEntity : configs) {
            oldResult.put(configEntity.getCode(), configEntity.getValue());
        }
        result = oldResult;
    }

    public static String getValue(String key) {
        return result.get(key);
    }

    public static String getValue(String key, String defaultValue) {
        String value = getValue(key);
        if(value == null) {
            return defaultValue;
        }
        return value;
    }



}
