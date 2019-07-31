package com.jupiter.web.manager.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * 会话帮助类
 *
 * @author TT
 */
@Component
public class SessionContextUtils {

    @Autowired
    private HttpServletRequest request;
    @Autowired
    private HttpServletResponse response;

    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 获取当前上下文请求
     *
     * @return HttpServletRequest
     * @throws
     * @Title: getContextRequest
     * @Description: TODO
     */
    public HttpServletRequest getContextRequest() {
        return request;
    }


    /**
     * 获取当前上下文请求
     *
     * @return HttpServletRequest
     * @throws
     * @Title: getContextRequest
     * @Description: TODO
     */
    public String getContextPath() {
        return request.getContextPath();
    }

    /**
     * 获取当前上下文请求
     *
     * @return HttpServletRequest
     * @throws
     * @Title: getContextRequest
     * @Description: TODO
     */
    public String getContextBasePath() {
        return getContextRequest().getScheme() + "://" + getContextRequest().getServerName() + ":" + getContextRequest().getServerPort() + getContextPath() + "/";
    }

    /**
     * 获取当前上下文响应
     *
     * @return HttpServletResponse
     * @throws
     * @Title: getContextResponse
     * @Description: TODO
     */
    public HttpServletResponse getContextResponse() {
        return response;
    }

    /**
     * 代替modelAndView的重定向(自动携带参数)
     *
     * @param url
     * @return void
     * @Author mudeliang
     * @date 2017/9/12 14:33
     **/
    public void redirect(String url) {
        redirect(url, null);
    }

    /**
     * 重定向
     *
     * @param url
     * @param params
     * @return void
     * @Author mudeliang
     * @date 2017/9/12 14:33
     **/
    public void redirect(String url, Map<String, Object> params) {
        StringBuffer path = new StringBuffer(url);
        if (params != null && params.size() > 0) {
            path.append("?");
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                path.append("&");
                path.append(entry.getKey());
                path.append("=");
                path.append(entry.getValue());
            }
            path.deleteCharAt(path.indexOf("&"));
        }
        try {
            response.sendRedirect(path.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取当前上下文SessionId
     *
     * @return String
     * @throws
     * @Title: getContextSessionId
     * @Description: TODO
     */
    public String getContextSessionId() {
        return this.request.getSession().getId();
    }

    /**
     * 获取当前Token
     *
     * @return String
     * @throws
     * @Title: getContextToken
     * @Description: TODO
     */
    public String getContextToken(String tokenName) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }
        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals(tokenName)) {
                return cookie.getValue();
            }
        }
        return null;
    }


    /**
     * 获取当前推荐码
     *
     * @return String
     * @throws
     * @Description: TODO
     */
    public String getRecommendCode() {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }
        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals("rcode")) {
                return cookie.getValue();
            }
        }
        return null;
    }

    /**
     * 删除当前推荐码
     *
     * @return String
     * @throws
     * @Description: TODO
     */
    public void removeRecommendCode() {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return;
        }
        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals("rcode")) {
                cookie.setPath("/");
                cookie.setMaxAge(0);
                cookie.setValue(null);
                response.addCookie(cookie);
            }
        }
    }


    /**
     * 获取当前Token
     *
     * @return String
     * @throws
     * @Title: getContextToken
     * @Description: TODO
     */
    public String getContextApiToken() {
        return request.getHeader("token");
    }


    /**
     * 获取当前apikey
     *
     * @return String
     * @throws
     * @Title: getContextToken
     * @Description: TODO
     */
    public String getContextApiKey() {
        return request.getHeader("apikey");
    }

    /**
     * 获取当前apisign
     *
     * @return String
     * @throws
     * @Title: getContextToken
     * @Description: TODO
     */
    public String getContextApiSign() {
        return request.getHeader("sign");
    }


    /**
     * 设置当前Token
     *
     * @return String
     * @throws
     * @Title: getContextToken
     * @Description: TODO
     */
    public void addContextToken(String domain, String tokenName, String token) {
        // 联合登录设置二级域名登录
        this.response.addHeader("Set-Cookie", String.format("%s=%s;Path=/; HttpOnly", tokenName, token));
        try {
            addSingleContextToken(domain, tokenName, token);
        } catch (Exception ex) {
            logger.error("联合登录设置二级域名登录", ex);
        }
    }

    /**
     * 设置当前Token
     *
     * @return String
     * @throws
     * @Title: getContextToken
     * @Description: TODO
     */
    public void addSingleContextToken(String domain, String tokenName, String token) {
        // 联合登录设置二级域名登录
        Cookie cookie = new Cookie(tokenName, token);
        if (domain != null) {
            cookie.setDomain(domain);
        }
        cookie.setPath("/");
        this.response.addCookie(cookie);
    }

    /**
     * 设置当前Token
     *
     * @return String
     * @throws
     * @Title: getContextToken
     * @Description: TODO
     */
    public void removeSingleContextToken(String domain, String tokenName) {
        // 联合登录设置二级域名登录
        Cookie cookie = new Cookie(tokenName, null);
        cookie.setDomain(domain);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        this.response.addCookie(cookie);
    }

    /**
     * 设置当前Token
     *
     * @return String
     * @throws
     * @Title: getContextToken
     * @Description: TODO
     */
    public void addCookieToken(String tokenName, String token) {
        // 联合登录设置二级域名登录
        addCookieTokenByDoamin(null, tokenName, token);
    }

    /**
     * 设置当前Token
     *
     * @return String
     * @throws
     * @Title: getContextToken
     * @Description: TODO
     */
    public void removeCookieToken(String tokenName) {
        // 联合登录设置二级域名登录
        removeCookieTokenByDoamin(null, tokenName);
    }

    /**
     * 设置当前Token
     *
     * @return String
     * @throws
     * @Title: getContextToken
     * @Description: TODO
     */
    public void addCookieTokenByDoamin(String domain, String tokenName, String token) {
        // 联合登录设置二级域名登录
        Cookie cookie = new Cookie(tokenName, token);
        if (domain != null) {
            cookie.setDomain(domain);
        }
        cookie.setPath("/");
        this.response.addCookie(cookie);
    }

    /**
     * 设置当前Token
     *
     * @return String
     * @throws
     * @Title: getContextToken
     * @Description: TODO
     */
    public void removeCookieTokenByDoamin(String domain, String tokenName) {
        // 联合登录设置二级域名登录
        Cookie cookie = new Cookie(tokenName, null);
        if (domain != null) {
            cookie.setDomain(domain);
        }
        cookie.setPath("/");
        cookie.setMaxAge(0);
        this.response.addCookie(cookie);
    }

}
