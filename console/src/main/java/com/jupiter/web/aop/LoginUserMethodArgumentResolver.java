package com.jupiter.web.aop;

import com.jupiter.web.manager.common.dao.UserDao;
import com.jupiter.web.manager.common.entity.User;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Slf4j
public class LoginUserMethodArgumentResolver implements HandlerMethodArgumentResolver {
    @Setter
    private UserDao userDao;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginUser.class);
    }


    @Override

    public User resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        LoginUser loginUser = parameter.getParameterAnnotation(LoginUser.class);
        User user = null;
        boolean require = loginUser.require();
        String sid = webRequest.getParameter("sid");
        if (require) {
            if (StringUtils.isBlank(sid)) {
                throw new RuntimeException("params_error");
            }
            user = userDao.getUserBySid(sid);
            if (user == null) {
                log.error("通过sid:{}获取用户失败", sid);
                throw new RuntimeException("E00060001");
            }
        } else {
            if (StringUtils.isNotBlank(sid)) {
                user = userDao.getUserBySid(sid);
            }
        }

        return user;
    }

}
