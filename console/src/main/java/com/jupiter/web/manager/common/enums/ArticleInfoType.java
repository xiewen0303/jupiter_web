package com.jupiter.web.manager.common.enums;

/**
 * Created by zhangxiqiang on 2019/7/23.
 */
public enum ArticleInfoType implements CodeEnum<Integer> {
    SAVE(1),PUBLISH(2);

    private Integer code;

    ArticleInfoType(Integer code) {
        this.code = code;
    }

    @Override
    public Integer getCode() {
        return code;
    }
}
