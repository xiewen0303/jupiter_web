package com.jupiter.web.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 系统拦截器
 *
 * @author TT
 */
@Slf4j
public class PrivilegeInteceptor implements HandlerInterceptor {

    private static ConcurrentHashMap<String, Long> userSessions = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<String, Long> loginSessions = new ConcurrentHashMap<>();


    public static void setLogin(String sessionId, Long adminId) {
        if (userSessions.get(sessionId) != null) {
            setLogout(adminId);

            loginSessions.put(sessionId, adminId);
        }
    }

    public static void setLogout(Long adminId) {
        String key = null;
        for (Map.Entry<String, Long> s : loginSessions.entrySet()) {
            if (s.getValue().equals(adminId)) {
                key = s.getKey();
            }
        }
        if (key != null) {
            loginSessions.put(key, 0L);
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
            throws Exception {

    }

    @Override
    public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
            throws Exception {

    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object arg2) throws Exception {
        String uri = request.getRequestURI();

        if (uri.contains("/jupiter/api")) {
            return true;
        }
        if (!uri.contains(".")) {
            log.info("================================url:{}", uri);
        }

        // 不接受任何jsp请求
        if (uri.endsWith(".jsp")) {
            return false;
        }
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setContentType("application/json;charset=UTF-8");
        response.setHeader("Cache-Control", "no-cache");
        HttpSession session = request.getSession();

        if (userSessions.get(session.getId()) == null) {
            userSessions.put(session.getId(), 0L);
        }

        if (loginSessions.containsKey(session.getId()) && loginSessions.get(session.getId()) == 0) {
//            session.invalidate();
            session.removeAttribute("login_admin");
            if (uri.indexOf("start/login/page") > -1) {
                loginSessions.remove(session.getId());
                return true;
            } else if (uri.indexOf("start/index") > -1) {
                userSessions.remove(session.getId());
                response.sendRedirect("/start/login/page");
            } else {
                //如果判断是 AJAX 请求,直接设置为session超时
                if (request.getHeader("web-requested-with") != null && request.getHeader("web-requested-with").equals("XMLHttpRequest")) {
                    response.setHeader("sessionstatus", "timeout");
                    response.sendError(518, "session timeout.");
                } else {
                    //不符合条件的，跳转到登录界面
                    userSessions.remove(session.getId());
                    response.sendRedirect("/start/login/page");
                }
            }
            //返回退出页面
            return false;
        }
        return true;
    }

}
