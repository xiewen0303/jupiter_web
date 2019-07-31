package com.jupiter.web.manager.common.enums;

/**
 * Created by zhangxiqiang on 2019/7/23.
 */
public enum LikeType implements CodeEnum<Integer> {
    ARTICLE(1),REVIEW(2);

    private Integer code;

    LikeType(Integer code) {
        this.code = code;
    }

    @Override
    public Integer getCode() {
        return code;
    }
}
