package com.jupiter.web.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by zhangxiqiang on 2019/7/23.
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface LoginUser {
    /**
     * 如果值为true, 用户信息没有获取成功要求登录
     * @return
     */
    boolean require() default true;
}
