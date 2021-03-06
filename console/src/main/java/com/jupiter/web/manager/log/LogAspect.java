package com.jupiter.web.manager.log;

import com.alibaba.fastjson.JSONObject;
import com.jupiter.web.manager.common.dao.LogDao;
import com.jupiter.web.manager.common.entity.Admin;
import com.jupiter.web.manager.common.entity.Log;
import com.jupiter.web.manager.bus.admin.dto.CommonResponse;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

@Aspect
@Component
public class LogAspect {

    @Autowired
    private LogDao logDao;

    @Pointcut("execution(* com.jupiter.web.manager.bus.*.controller..*(..)) && @annotation(com.jupiter.web.manager.log.LogAno)")
    public void logPointCut() {
    }

//    @After("logPointCut()")
//    public void doAfter(JoinPoint joinPoint) {
//        System.out.println("after: " + joinPoint);
//        for (Object obj : joinPoint.getArgs()) {
//            System.out.println(obj);
//        }
//    }

    private Method getMethod(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        return method;
    }

    @AfterReturning(returning = "ret", pointcut = "logPointCut()")
    public void doAfterReturn(JoinPoint joinPoint, Object ret) {
        if (ret instanceof CommonResponse) {
            CommonResponse result = (CommonResponse) ret;
            if (result.getCode().equals("200")) {
                doLog(joinPoint);
            }
        } else {
            doLog(joinPoint);
        }
    }

    private void doLog(JoinPoint joinPoint) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Admin admin = (Admin) request.getSession().getAttribute("login_admin");
        Method method = getMethod(joinPoint);
        LogAno logAno = method.getAnnotation(LogAno.class);

        String logInfo = null;
        if (!logAno.isPrivateLog() && joinPoint.getArgs() != null && joinPoint.getArgs().length > 0) {
            Object arg = joinPoint.getArgs()[0];
            logInfo = JSONObject.toJSONString(arg);
        }
        saveLog(admin.getId(), admin.getAdminName(), logAno.logType(), logInfo, request.getRequestURI());
    }

    public void saveLog(long adminId, String adminName, LogType logType, String logInfo, String logUri) {
        Log log = new Log();
        log.setAdminId(adminId);
        log.setAdminName(adminName);
        log.setLogType(logType.getType());
        log.setLogInfo(logInfo);
        log.setLogUri(logUri);
        log.setLogTime(System.currentTimeMillis());
        logDao.insert(log);
    }

}
