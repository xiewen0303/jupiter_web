package com.jupiter.web.manager.common.enums;

/**
 * @author fuyuling
 * @created 2019/7/25 15:32
 */
public enum LikeEnum {

    LIKE(1), DISLIKE(0);

    private int code;

    LikeEnum(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

}
