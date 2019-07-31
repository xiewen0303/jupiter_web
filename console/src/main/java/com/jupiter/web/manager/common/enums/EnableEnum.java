package com.jupiter.web.manager.common.enums;

/**
 * Created by zhangxiqiang on 2019/7/23.
 */
public enum EnableEnum implements CodeEnum<Integer> {
    ENABLE(1),UNABLE(0);

    private Integer code;

    EnableEnum(Integer code) {
        this.code = code;
    }

    @Override
    public Integer getCode() {
        return code;
    }
}
