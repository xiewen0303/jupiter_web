package com.jupiter.web.manager.bus.admin.controller;

import com.jupiter.web.manager.bus.admin.dto.CommonResponse;
import com.jupiter.web.manager.bus.admin.service.AdminService;
import com.jupiter.web.manager.common.entity.Admin;
import com.jupiter.web.manager.constants.Constant;
import com.jupiter.web.manager.log.LogAno;
import com.jupiter.web.manager.log.LogType;
import com.jupiter.web.manager.redis.RedisConstants;
import com.jupiter.web.manager.redis.RedisService;
import com.jupiter.web.manager.utils.Utils;
import com.jupiter.web.manager.utils.VerifyCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("start")
public class IndexController extends BaseController {
    private static Logger logger = LoggerFactory.getLogger(IndexController.class);

    @Autowired
    private AdminService adminService;

    @Autowired
    private RedisService redisService;

    /**
     * 登录页面
     */
    @RequestMapping("/login/page")
    public String loginPage() throws Exception {
        return "login";
    }

    @RequestMapping("/validateImage")
    public ModelAndView getKaptchaImage() throws Exception {
        HttpServletResponse response = sessionContextUtils.getContextResponse();
        response.setDateHeader("Expires", 0);
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        response.setContentType("img/jpeg");
        String verifyCode = VerifyCodeUtils.generateVerifyCode(4);
        deletVerifyCode();
        setVerifyCode(verifyCode);

        // 生成图片
        int w = 100, h = 41;
        VerifyCodeUtils.outputImage(w, h, response.getOutputStream(), verifyCode);
        return null;
    }

    /**
     * 删除缓存中的图片验证码
     */
    private void deletVerifyCode() {
        String token = sessionContextUtils.getContextToken("CHECKCODE");
        if (token != null && !"".equals(token)) {
            redisService.delete(token);
        }
    }

    /**
     * 将图片验证码保存到缓存中,便于登录时的校验
     *
     * @param verifyCode
     */
    private void setVerifyCode(String verifyCode) {
        try {
            String token = RedisConstants.VERIFYCODE_KEY + Utils.encodeMD5(Utils.getRandomString(6) + new Timestamp(new Date().getTime()));
            if (!redisService.set(token, verifyCode, Constant.IMAGESEXPIRETIME)) {
                throw new RuntimeException("图片验证码缓存失败");
            }
            HttpServletRequest request = sessionContextUtils.getContextRequest();
            sessionContextUtils.addContextToken(null, "CHECKCODE", token);

        } catch (Exception e) {
            logger.error("图片验证码缓存key生成失败", e);
        }
    }

    /**
     * 首页
     */
    @RequestMapping("/index")
    public ModelAndView index() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ModelAndView modelAndView = new ModelAndView();
        HttpServletRequest request = sessionContextUtils.getContextRequest();
        modelAndView.addObject("dateTime", sdf.format(new Date()));
        modelAndView.addObject("login_admin", request.getSession().getAttribute("login_admin"));
        modelAndView.setViewName("index");
        return modelAndView;
    }

    /**
     * 登录跳转
     *
     * @param requestMap
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("/login")
    @LogAno(logType = LogType.LOGIN, isPrivateLog = true)
    public CommonResponse login(@RequestBody Map<String, String> requestMap) throws Exception {
        CommonResponse result = new CommonResponse();
        String adminName = requestMap.get("adminName");
        String password = requestMap.get("password");
//        String vcode = requestMap.get("vcode");
        if (StringUtils.isEmpty(adminName) || StringUtils.isEmpty(password)) {
//            if (StringUtils.isEmpty(adminName) || StringUtils.isEmpty(password) || StringUtils.isEmpty(vcode)) {
            result.setCode("300");
            result.setMsg("请输入帐号，密码，图片验证码");
            deletVerifyCode();
            return result;
        } else {
//            TODO wind 图片验证码
//            if (!getVerifyCode(vcode)) {
//                result.setCode("300");
//                result.setMsg("图片验证码错误！");
//                deletVerifyCode();
//                return result;
//            }
            // 登录判断
            Subject admin = SecurityUtils.getSubject();

            String salt = getSaltByName(adminName);
            if (StringUtils.isEmpty(salt)) {
                result.setCode("300");
                result.setMsg("用户名或密码不存在,请重新输入");
                return result;
            }
            UsernamePasswordToken token = new UsernamePasswordToken(adminName, Utils.encodeMD5(password.concat(salt)));
            token.setRememberMe(true);
            try {
                // 登录
                admin.login(token);
            } catch (Exception e) {
                logger.error("error.", e);
                token.clear();
                result.setCode("300");
                result.setMsg(e.getMessage());
                deletVerifyCode();
                return result;
            }
        }

        result.setCode("200");
        result.setMsg("登录成功");
        deletVerifyCode();
        return result;
    }

    /**
     * 根据用户名查询盐值用于密码加密
     *
     * @param adminName
     * @return
     */
    private String getSaltByName(String adminName) {
        Admin adminCondition = new Admin();
        adminCondition.setAdminName(adminName);
        Admin adminInfo = adminService.getAdminByProperty(adminCondition);
        if (adminInfo == null) {
            return null;
        }
        return adminInfo.getSalt();
    }

    /**
     * 获取缓存数据
     */
    private boolean getVerifyCode(String code) {
        String vcode = getRedisData("CHECKCODE");
        if (vcode != null && vcode.toLowerCase().equals(code.toLowerCase())) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取缓存数据
     */
    private String getRedisData(String resetToken) {
        String token = sessionContextUtils.getContextToken(resetToken);
        if (token == null) {
            return null;
        }
        Object rest = redisService.get(token);
        if (StringUtils.isEmpty(rest)) {
            return null;
        }
        return rest.toString();
    }

//    @LogAno(logType = LogType.LOGOUT)
//    @RequestMapping("/logout")
//    public ModelAndView logout() throws Exception {
//        ModelAndView modelAndView = new ModelAndView();
//        Subject admin = SecurityUtils.getSubject();
//        admin.getSession().removeAttribute("permissions");
//        admin.logout();
//        modelAndView.setViewName("redirect:/start/login/page");
//        return modelAndView;
//    }
}
