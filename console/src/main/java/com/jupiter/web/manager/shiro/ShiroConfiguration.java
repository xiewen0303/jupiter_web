package com.jupiter.web.manager.shiro;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import java.util.LinkedHashMap;
import java.util.Map;


/**
 * shiro配置类
 * Created by cdyoue on 2016/10/21.
 */
@Configuration
public class ShiroConfiguration {
    /**
     * LifecycleBeanPostProcessor，这是个DestructionAwareBeanPostProcessor的子类，
     * 负责org.apache.shiro.util.Initializable类型bean的生命周期的，初始化和销毁。
     * 主要是AuthorizingRealm类的子类，以及EhCacheManager类。
     */
    @Bean(name = "lifecycleBeanPostProcessor")
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    /**
     * HashedCredentialsMatcher，这个类是为了对密码进行编码的，
     * 防止密码在数据库里明码保存，当然在登陆认证的时候，
     * 这个类也负责对form里输入的密码进行编码。
     */
    @Bean(name = "hashedCredentialsMatcher")
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
        credentialsMatcher.setHashAlgorithmName("MD5");
        credentialsMatcher.setHashIterations(2);
        credentialsMatcher.setStoredCredentialsHexEncoded(true);
        return credentialsMatcher;
    }

    /**
     * ShiroDbRealm， ，继承自AuthorizingRealm，
     * 负责用户的认证和权限的处理，可以参考JdbcRealm的实现。
     */
    @Bean(name = "shiroDbRealm")
    @DependsOn("lifecycleBeanPostProcessor")
    public ShiroDbRealm shiroDbRealm() {
        ShiroDbRealm realm = new ShiroDbRealm();
        return realm;
    }

    /**
     * SecurityManager，权限管理，这个类组合了登陆，登出，权限，session的处理，是个比较重要的类。
     */
    @Bean(name = "securityManager")
    public DefaultWebSecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(shiroDbRealm());
        return securityManager;
    }

    /**
     * ShiroFilterFactoryBean，是个factorybean，为了生成ShiroFilter。
     * 它主要保持了三项数据，securityManager，filters，filterChainDefinitionManager。
     */
    @Bean(name = "shiroFilter")
    public ShiroFilterFactoryBean shiroFilterFactoryBean() {
//        anon 未认证可以访问   authc 认证后可以访问     perms 需要特定权限才能访问     roles 需要特定角色才能访问
//        user 需要特定用户才能访问    port 需要特定端口才能访问    reset 根据指定 HTTP 请求访问才能访问

        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager());
        //设置登录页
        shiroFilterFactoryBean.setLoginUrl("/start/login/page");
        //设置无权限时跳转的页面
        shiroFilterFactoryBean.setUnauthorizedUrl("/start/login/page");

        Map<String, String> filterChainDefinitionManager = new LinkedHashMap<>();
        //退出登录
        filterChainDefinitionManager.put("/start/logout", "logout");
        //登录页相关,开放权限
        filterChainDefinitionManager.put("/start/login/page", "anon");
        filterChainDefinitionManager.put("/start/login", "anon");
        filterChainDefinitionManager.put("/start/validateImage", "anon");
        //前端文件,开放权限
        filterChainDefinitionManager.put("/css/**", "anon");
        filterChainDefinitionManager.put("/error/**", "anon");
        filterChainDefinitionManager.put("/fonts/**", "anon");
        filterChainDefinitionManager.put("/img/**", "anon");
        filterChainDefinitionManager.put("/js/**", "anon");
        filterChainDefinitionManager.put("/plugins/**", "anon");
        filterChainDefinitionManager.put("/ueditor/**", "anon");

        //刷新配置开放权限
        filterChainDefinitionManager.put("/config/reloadConfig", "anon");
        filterChainDefinitionManager.put("/jupiter/**", "anon");
        //其他所有页面都需要权限验证
        filterChainDefinitionManager.put("/**", "authc,perms");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionManager);

        // 登录成功后要跳转的链接
        shiroFilterFactoryBean.setSuccessUrl("index");

        return shiroFilterFactoryBean;
    }

    /**
     * DefaultAdvisorAutoProxyCreator，Spring的一个bean，由Advisor决定对哪些类的方法进行AOP代理。
     */
    @Bean
    @ConditionalOnMissingBean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAAP = new DefaultAdvisorAutoProxyCreator();
        defaultAAP.setProxyTargetClass(true);
        return defaultAAP;
    }

    /**
     * AuthorizationAttributeSourceAdvisor，shiro里实现的Advisor类，
     * 内部使用AopAllianceAnnotationsAuthorizingMethodInterceptor来拦截用以下注解的方法。
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor() {
        AuthorizationAttributeSourceAdvisor aASA = new AuthorizationAttributeSourceAdvisor();
        aASA.setSecurityManager(securityManager());
        return aASA;
    }

    @Bean
    public ShiroDialect shiroDialect() {
        return new ShiroDialect();
    }
}
