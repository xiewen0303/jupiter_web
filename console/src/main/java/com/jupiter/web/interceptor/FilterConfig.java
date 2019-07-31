package com.jupiter.web.interceptor;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean buildEncryFilter(EncryFilter encryFilter) {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setOrder(1);
        filterRegistrationBean.setFilter(encryFilter);
        return filterRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean buildSessionFilter(SessionFilter sessionFilter) {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setOrder(2);
        filterRegistrationBean.setFilter(sessionFilter);
        return filterRegistrationBean;
    }
}
