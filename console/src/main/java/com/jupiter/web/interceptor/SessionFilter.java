package com.jupiter.web.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.jupiter.web.manager.common.dao.LoginSessionDao;
import com.jupiter.web.manager.common.entity.LoginSession;
import com.jupiter.web.manager.common.locale.DefaultLocale;
import com.tron.framework.dto.ResponseEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
@Component
public class SessionFilter extends OncePerRequestFilter {

    @Autowired
    private AppCofig appCofig;

    @Autowired
    private LoginSessionDao loginSessionDao;

    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String header = request.getHeader("XENCR");
        if (header != null && header.equals("1")) {
            // 只有正式使用java服务器才进这里
            String path = request.getRequestURI().replace("/", "");
            boolean abort_flag = false;
            // request.getSession().setAttribute("sid", "");
            String sid = request.getParameter("sid");
            if (appCofig.getSession_url().containsKey(path)) {
                LoginSession loginSession = loginSessionDao.getById(sid);
                if (loginSession == null) {
                    if (getUrlFlag(path)) { // 暂时看起来都是false
                        abort_flag = true;
                    }
                } else {
                    long last_time = loginSession.getLastActionTime();
                    long now = System.currentTimeMillis();
                    long interval = appCofig.getSession_valid_time() * 60 * 1000;
                    if (last_time + interval > now) {
                        loginSession.setLastActionTime(now);
                        loginSessionDao.update(loginSession);
                        // request.getSession().setAttribute("sid", sid);
                        logger.info("+++++++++++++ session valid:" + sid);
                    } else {
                        loginSessionDao.delete(sid);
                        if (getUrlFlag(path)) {
                            abort_flag = true;
                        }
                    }
                }
            }
            if (abort_flag) {
                logger.info("+++++++++++++ session Not valid, aborting, sid:" + sid);
                ResponseEntity entity = new ResponseEntity();
                entity.setStatus(ResponseEntity.STATUS_FAIL);
                entity.setError("E00060001");
                entity.setMsg(DefaultLocale.get("E00060001"));
                returnJson(response, entity);
                return;

            }
        }
        chain.doFilter(request, response);
    }

    private void returnJson(ServletResponse response, ResponseEntity entity) {
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            String json = JSONObject.toJSONString(entity);
            writer.print(json);
        } catch (IOException e) {
            log.error("response error", e);
        } finally {
            if (writer != null)
                writer.close();
        }
    }

    private boolean getUrlFlag(String path) {
        String flag = appCofig.getSession_url().get(path);
        return Boolean.valueOf(flag);
    }

    @Override
    public void destroy() {
    }

}