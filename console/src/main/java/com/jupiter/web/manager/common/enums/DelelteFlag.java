package com.jupiter.web.manager.common.enums;

/**
 * Created by zhangxiqiang on 2019/7/23.
 */
public enum DelelteFlag implements CodeEnum<Integer> {
    DELETE(1),AVAILABLE(0);

    private Integer code;

    DelelteFlag(Integer code) {
        this.code = code;
    }

    @Override
    public Integer getCode() {
        return code;
    }
}
