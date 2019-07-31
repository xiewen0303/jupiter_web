package com.jupiter.web.manager.redis;

public class RedisConstants {

    public static final String KEY_ORDER_GENERATE = "KEY_ORDER_GENERATE:";

    public static String getMerchantKeyOrderGenerate(Long merchantId, String orderId) {
        return KEY_ORDER_GENERATE + merchantId + ":" + orderId;
    }

    /**
     * 图片验证码的标志位
     */
    public static String VERIFYCODE_KEY = "VERIFYCODE_";
}
