package com.jupiter.web.manager.bus.appUser.service;


import com.jupiter.web.controller.TestEntity;
import com.jupiter.web.interceptor.AppCofig;
import com.jupiter.web.manager.bus.admin.store.ConfigManager;
import com.jupiter.web.manager.bus.appUser.dto.CaptchaDto;
import com.jupiter.web.manager.bus.appUser.dto.PwdDto;
import com.jupiter.web.manager.bus.appUser.dto.UserActionType;
import com.jupiter.web.manager.bus.appUser.dto.UserDto;
import com.jupiter.web.manager.common.dao.CaptchaDao;
import com.jupiter.web.manager.common.dao.LoginInfoDao;
import com.jupiter.web.manager.common.dao.LoginSessionDao;
import com.jupiter.web.manager.common.dao.UserDao;
import com.jupiter.web.manager.common.entity.Captcha;
import com.jupiter.web.manager.common.entity.LoginInfo;
import com.jupiter.web.manager.common.entity.LoginSession;
import com.jupiter.web.manager.common.entity.User;
import com.jupiter.web.manager.common.locale.DefaultLocale;
import com.jupiter.web.manager.utils.GenIdTools;
import com.jupiter.web.manager.utils.Utils;
import com.tron.framework.dto.ResponseEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private CaptchaDao captchaDao;

    @Autowired
    private LoginInfoDao loginInfoDao;

    @Autowired
    private LoginSessionDao loginSessionDao;

    @Autowired
    private SMSService smsService;

    @Value("${app-config.captcha_valid_time}")
    private int captchaValidTime;

    public ResponseEntity login(UserDto userDto, String ip) {
        String mobile = userDto.getMobile();
        String captcha = userDto.getCaptcha();
        String password = userDto.getPassword();
        if (isParamEmpty(mobile) || !checkMobile(mobile)) {
            return new ResponseEntity(ResponseEntity.STATUS_FAIL, null, DefaultLocale.get("mobile.wrong"), null);
        }
        if (isParamEmpty(mobile) && isParamEmpty(password)) {
            return new ResponseEntity(ResponseEntity.STATUS_FAIL, null, DefaultLocale.get("captcha.password.needed"), null);
        }
        int login_result = 3;
        User dbData = new User();
        dbData.setMobile(mobile);
        User oldUser = userDao.getOne(dbData);
        LoginInfo loginInfo = new LoginInfo();
        if (oldUser == null) {
            log.info("未注册的手机号, do register mobile:" + mobile);
            if (isParamEmpty(captcha)) {
                return new ResponseEntity(ResponseEntity.STATUS_FAIL, null, DefaultLocale.get("captcha.needed"), null);
            }
            if (isParamEmpty(password)) {
                return new ResponseEntity(ResponseEntity.STATUS_FAIL, null, DefaultLocale.get("captcha.needed"), null);
            }
            if (!isValid(mobile, captcha, UserActionType.LOGIN.getName())) {
                return new ResponseEntity(ResponseEntity.STATUS_FAIL, null, DefaultLocale.get("captcha.wrong"), null);
            }
            User user = new User();
            user.setId(GenIdTools.getUUId());
            user.setCreateTime(System.currentTimeMillis());
            doPwd(user, password);
            user.loadByDto(userDto, ip);
            userDao.insert(user);

            loginInfo.setId(GenIdTools.getUUId());
            login_result = 1;
            loginInfo.setResult(login_result);
            loginInfo.loadByUser(userDto, ip);
            loginInfoDao.insert(loginInfo);

            LoginSession loginSession = new LoginSession();
            loginSession.setUserId(user.getId());
            loginSession.setAddProduct(userDto.getAdd_product());
            loginSession.setId(GenIdTools.getUUId());
            loginSession.setSid(GenIdTools.getUUId());
            loginSession.setAddChannel(userDto.getAdd_channel());
            loginSession.setLastActionTime(System.currentTimeMillis());
            loginSessionDao.insert(loginSession);

            userDto.setPassword("");
            userDto.setSid(loginSession.getId());
            return new ResponseEntity(ResponseEntity.STATUS_OK, null, DefaultLocale.get("ok"), userDto);
        } else {
            log.debug("已注册的手机号, mobile:" + mobile);
            String error_msg = DefaultLocale.get("error");
            //user.invite_code = old.invite_code
            if (oldUser.getWriteOffFlag() == 1) {
                login_result = 4;
            } else {
                if (isParamEmpty(captcha)) {
                    log.debug("check login captcha, captcha:%s" + captcha);
                    if (isValid(mobile, captcha, UserActionType.LOGIN.getName())) {
                        login_result = 1;
                    } else {
                        error_msg = DefaultLocale.get("captcha.wrong");
                    }
                } else {
                    error_msg = DefaultLocale.get("password.wrong");
                    if (checkPassword(oldUser.getSalt(), password, oldUser.getPassword())) {
                        login_result = 1;
                    }
                }
            }

            loginInfo.setId(GenIdTools.getUUId());
            loginInfo.setResult(login_result);
            loginInfo.loadByUser(userDto, ip);
            loginInfoDao.insert(loginInfo);

            if (login_result == 1) { //login OK
                loginSessionDao.deleteByParam(oldUser.getId(), userDto.getAdd_channel());

                LoginSession loginSession = new LoginSession();
                loginSession.setUserId(oldUser.getId());
                loginSession.setAddProduct(userDto.getAdd_product());
                loginSession.setId(GenIdTools.getUUId());
                loginSession.setSid(GenIdTools.getUUId());
                loginSession.setAddChannel(userDto.getAdd_channel());
                loginSession.setLastActionTime(System.currentTimeMillis());
                loginSessionDao.insert(loginSession);

                userDto.setPassword("");
                userDto.setSid(loginSession.getId());
                return new ResponseEntity(ResponseEntity.STATUS_OK, null, DefaultLocale.get("ok"), userDto);
            } else {
                return new ResponseEntity(ResponseEntity.STATUS_FAIL, null, error_msg, null);
            }
        }
    }

    public ResponseEntity logout(String sid) {
        loginSessionDao.deleteBySid(sid);
        return new ResponseEntity(ResponseEntity.STATUS_OK, null, DefaultLocale.get("ok"), null);
    }

    public ResponseEntity changePwd(PwdDto pwdDto) {
        String password = pwdDto.getPassword();
        String sid = pwdDto.getSid();
        if (isParamEmpty(sid)) {
            String mobile = pwdDto.getMobile();
            String captcha = pwdDto.getCaptcha();
            if (isParamEmpty(mobile)) {
                return new ResponseEntity(ResponseEntity.STATUS_FAIL, null, DefaultLocale.get("mobile.wrong"), null);
            }
            User user = new User();
            user.setMobile(mobile);
            user = userDao.getOne(user);
            if (user == null) {
                return new ResponseEntity(ResponseEntity.STATUS_FAIL, null, DefaultLocale.get("mobile.wrong"), null);
            }
            if (!isValid(mobile, captcha, UserActionType.CHANGE_PWD.getName())) {
                return new ResponseEntity(ResponseEntity.STATUS_FAIL, null, DefaultLocale.get("captcha.wrong"), null);
            }
            doPwd(user, password);
            userDao.update(user);
            return new ResponseEntity(ResponseEntity.STATUS_OK, null, DefaultLocale.get("ok"), null);
        } else {
            String old_password = pwdDto.getOld_password();
            LoginSession loginSession = new LoginSession();
            loginSession.setSid(sid);
            loginSession = loginSessionDao.getOne(loginSession);
            if (loginSession == null) {
                return new ResponseEntity(ResponseEntity.STATUS_FAIL, null, DefaultLocale.get("password.wrong"), null);
            }
            User user = userDao.getById(loginSession.getUserId());
            if (user == null) {
                return new ResponseEntity(ResponseEntity.STATUS_FAIL, null, DefaultLocale.get("password.wrong"), null);
            }
            if (!checkPassword(user.getSalt(), old_password, user.getPassword())) {
                return new ResponseEntity(ResponseEntity.STATUS_FAIL, null, DefaultLocale.get("password.wrong"), null);
            }
            doPwd(user, password);
            userDao.update(user);
            return new ResponseEntity(ResponseEntity.STATUS_OK, null, DefaultLocale.get("ok"), null);
        }
    }

    private void doPwd(User user, String password) {
        String salt = GenIdTools.getUUId();
        user.setSalt(salt);
        String md5pwd = getMD5Password(salt, password);
        user.setPassword(md5pwd);
    }

    public ResponseEntity sendVCode(CaptchaDto captchaDto, String ip) {
        String mobile = captchaDto.getMobile();
        String type = captchaDto.getType();
        String sendType = captchaDto.getSend_type();
        if (isParamEmpty(sendType)) {
            sendType = "sms";
        }
        if (isParamEmpty(mobile) || !checkMobile(mobile)) {
            return new ResponseEntity(ResponseEntity.STATUS_FAIL, null, DefaultLocale.get("mobile.wrong"), null);
        }
        if (!UserActionType.checkType(type)) {
            return new ResponseEntity(ResponseEntity.STATUS_FAIL, null, DefaultLocale.get("error"), null);
        }
        if (!"sms".equals(sendType)) {
            return new ResponseEntity(ResponseEntity.STATUS_FAIL, null, DefaultLocale.get("error"), null);
        }
        //1:  需要在手机号前面拼接国际区号，2:  0开头的手机号需要去掉0（针对印尼手机号）
        String locale = ConfigManager.getValue("locale");
        if (mobile.startsWith("0")) {
            mobile = mobile.substring(1);
        }
        int code = new Random().nextInt(9000) + 1000;
        String content;
        if ("id_ID".equals(locale)) {
            mobile = "62" + mobile;
            content = "Kode Verifikasi Daftar : " + code + ", Kode Berlaku Dalam 5 Menit";
        } else if ("en_IN".equals(locale)) {
            mobile = "91" + mobile;
            content = "【COCOK!】Your verification code is: " + code + ", valid within 5 minutes";
        } else {
            content = "Your verification code is: " + code + ", valid within 5 minutes";
        }
        log.debug("send sms content : " + content);

        smsService.sendSMS(mobile, content, 1);

        Captcha captcha = new Captcha();
        captcha.setClientIp(ip);
        captcha.setType(type);
        captcha.setCode(code + "");
        captcha.setMobile(mobile);
        captcha.setValidTime(captchaValidTime);
        captchaDao.insert(captcha);
        return new ResponseEntity(ResponseEntity.STATUS_OK, null, DefaultLocale.get("ok"), null);
    }

    public ResponseEntity querySMS(TestEntity testEntity) {
        log.debug("testEntity-------------- " + testEntity);
        ResponseEntity entity = new ResponseEntity();
        entity.setMsg("test啦啦啦");
        entity.setStatus(1);
        testEntity.setImei("测试哦");
        entity.setData(testEntity);
        return entity;
    }

    private boolean checkMobile(String mobile) {
        Pattern pattern = Pattern.compile("^[0-9]{5,15}$");
        Matcher m = pattern.matcher(mobile);
        return m.lookingAt();
    }

    private boolean isParamEmpty(String param) {
        return param == null || param.isEmpty();
    }

    private boolean isWhiteMobile(String mobile) {
        if (isParamEmpty(mobile)) {
            return false;
        }
        String whiteMobile = ConfigManager.getValue("sms_exemption_mobile");
        String[] arr = whiteMobile.split(",");
        for (String white : arr) {
            if (white.equals(mobile)) {
                return true;
            }
        }
        return false;
    }

    private boolean isValid(String mobile, String code, String type) {
        if (isWhiteMobile(mobile)) {
            return true;
        }
        if (isParamEmpty(code)) {
            return false;
        }
        Captcha captcha = new Captcha();
        captcha.setMobile(mobile);
        captcha.setCode(code);
        captcha.setType(type);
        captcha = captchaDao.getOne(captcha);
        if (captcha == null) {
            log.info("captcha NOT exist, mobile:" + mobile + ", code:" + code + ", type:" + type);
            return false;
        }
        long validTime = captcha.getAddTime() + captcha.getValidTime() * 1000;
        if (validTime < System.currentTimeMillis()) {
            log.info("captcha time expire, mobile:" + mobile + ", code:" + code + ", type:" + type);
            return false;
        }
        log.info("captcha ok, mobile:" + mobile + ", code:" + code + ", type:" + type);
        return true;
    }


    private boolean checkPassword(String salt, String password, String oldPassword) {
        String md5pwd = getMD5Password(salt, password);
        return md5pwd.equals(oldPassword);
    }

    private String getMD5Password(String salt, String password) {
        String reverseSalt = new StringBuilder(salt).reverse().toString();
        return Utils.encodeMD5(reverseSalt + password + salt);
    }
}
