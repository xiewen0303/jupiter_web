package com.jupiter.web.manager.common.enums;

/**
 * Created by zhangxiqiang on 2019/7/23.
 */
public enum ArticleInfoStatus implements CodeEnum<Integer> {
    FAIL(1),DRAFT(2),AUDITING(3),SUCCESS(4);

    private Integer code;

    ArticleInfoStatus(Integer code) {
        this.code = code;
    }

    @Override
    public Integer getCode() {
        return code;
    }
}
