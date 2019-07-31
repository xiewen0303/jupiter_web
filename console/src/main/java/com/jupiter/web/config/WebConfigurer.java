package com.jupiter.web.config;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.google.common.collect.Lists;
import com.jupiter.web.aop.LoginUserMethodArgumentResolver;
import com.jupiter.web.interceptor.PrivilegeInteceptor;
import com.jupiter.web.manager.bus.admin.store.ConfigManager;
import com.jupiter.web.manager.common.dao.UserDao;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.lang.Nullable;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;

import java.util.List;
import java.util.Locale;

@Configuration
public class WebConfigurer implements WebMvcConfigurer {

    @Autowired
    private MessageSource messageSource;
    @Autowired
    private UserDao userDao;


    // 这个方法是用来配置静态资源的，比如html，js，css，等等
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
    }

    // 这个方法用来注册拦截器，我们自己写好的拦截器需要通过这里添加注册才能生效
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // addPathPatterns("/**") 表示拦截所有的请求，
        // excludePathPatterns("/login", "/register") 表示除了登陆与注册之外，因为登陆注册不需要登陆也可以访问
        // registry.addInterceptor(encryInterceptor).addPathPatterns("/**").excludePathPatterns("/login", "/register");
        // addPathPatterns 用来设置拦截路径，excludePathPatterns 用来设置白名单，也就是不需要触发这个拦截器的路径
//        registry.addInterceptor(encryInterceptor).addPathPatterns("/jupiter/**");
        registry.addInterceptor(new PrivilegeInteceptor()).addPathPatterns("/**").excludePathPatterns("/jupiter");
    }

    // param-------------- {"addChannel":"app","addProduct":"India","devicePlatform":"android","marketChannel":"google_play","mobile":"1861708643","password":"33333","productVersion":"1.0.0"}
    //key-------------- uxc4REON2aCMgd66

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        FastJsonHttpMessageConverter jsonConverter = new FastJsonHttpMessageConverter();
        jsonConverter.setSupportedMediaTypes(Lists.newArrayList(MediaType.APPLICATION_JSON, MediaType.TEXT_HTML));
        jsonConverter.setFeatures(SerializerFeature.PrettyFormat, SerializerFeature.WriteDateUseDateFormat);
        converters.add(jsonConverter);
    }

    @Bean
    public LocaleResolver localeResolver() {
        CookieLocaleResolver localeResolver = new CookieLocaleResolver();
        //设置默认区域

        String language = ConfigManager.getValue("locale");
        if(StringUtils.isBlank(language)) {
            localeResolver.setDefaultLocale(Locale.ENGLISH);
        } else {
            localeResolver.setDefaultLocale(new Locale(language));
        }
        return localeResolver;
    }


    @Nullable
    @Override
    public Validator getValidator() {
        return validator();
    }

    @Bean
    public Validator validator() {
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.setValidationMessageSource(messageSource);
        return validator;
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("forward:start/login/page");
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
        //super.addViewControllers(registry);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        LoginUserMethodArgumentResolver loginUserMethodArgumentResolver = new LoginUserMethodArgumentResolver();
        loginUserMethodArgumentResolver.setUserDao(userDao);
        argumentResolvers.add(loginUserMethodArgumentResolver);
    }


}
