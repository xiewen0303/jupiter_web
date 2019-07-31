package com.jupiter.web.manager.common.locale;

import com.tron.framework.context.SpringUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.lang.Nullable;

import java.util.Locale;

/**
 * Created by zhangxiqiang on 2019/7/22.
 * 国际化
 */
public class DefaultLocale {

    /**
     * 获取国际化message
     * @param key
     * @return
     */
    public static String get(String key) {
        return get(key, null);
    }

    public static String get(String key, @Nullable Object[] args) {
        if(StringUtils.isBlank(key)) {
            return null;
        }
        MessageSource messageSource = SpringUtil.getBean(MessageSource.class);
        return messageSource.getMessage(key, args, LocaleContextHolder.getLocale());
    }

    /**
     * 获取指定的locale的国际化message
     * @param key
     * @param locale
     * @return
     */
    public static String getByLocale(String key, Locale locale) {
        return getByLocale(key, locale, null);
    }

    public static String getByLocale(String key, Locale locale, @Nullable Object[] args) {
        if(StringUtils.isBlank(key)) {
            return null;
        }
        MessageSource messageSource = SpringUtil.getBean(MessageSource.class);
        return messageSource.getMessage(key, args, locale);
    }

}
