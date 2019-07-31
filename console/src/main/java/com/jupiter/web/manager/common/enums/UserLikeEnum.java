package com.jupiter.web.manager.common.enums;

/**
 * 用户当前是否点赞，YES点赞，NO未点赞
 * @author fuyuling
 * @created 2019/7/25 15:32
 */
public enum UserLikeEnum {

    YES(1), NO(0);

    private int code;

    UserLikeEnum(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

}
