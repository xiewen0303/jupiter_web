package com.jupiter.web.manager.bus.appUser.controller;

import com.jupiter.web.controller.TestEntity;
import com.jupiter.web.manager.bus.appUser.dto.CaptchaDto;
import com.jupiter.web.manager.bus.appUser.dto.PwdDto;
import com.jupiter.web.manager.bus.appUser.dto.UserDto;
import com.jupiter.web.manager.bus.appUser.service.UserService;
import com.tron.framework.dto.ResponseEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@Slf4j
@RequestMapping("/jupiter/api")
public class UserController {

    @Autowired
    private UserService userService;


    @RequestMapping("/user/login")
    public ResponseEntity login(HttpServletRequest request, UserDto userDto) {
        String ip = request.getRemoteAddr();
        System.out.println("ip " + ip);
        return userService.login(userDto, ip);
    }

    @RequestMapping("/user/logout")
    public ResponseEntity logout(String sid) {
        return userService.logout(sid);
    }

    @RequestMapping("/user/change_pwd")
    public ResponseEntity changePwd(PwdDto pwdDto) {
        return userService.changePwd(pwdDto);
    }

    @RequestMapping("/message/send_vcode")
    public ResponseEntity sendVCode(HttpServletRequest request, CaptchaDto captchaDto) {
        String ip = request.getRemoteAddr();
        System.out.println("ip " + ip);
        return userService.sendVCode(captchaDto, ip);
    }

    @RequestMapping("message/query_sms")
    public ResponseEntity querySMS(TestEntity testEntity) {
        log.debug("testEntity-------------- " + testEntity);
        ResponseEntity entity = new ResponseEntity();
        entity.setMsg("test啦啦啦");
        entity.setStatus(1);
        testEntity.setImei("测试哦");
        entity.setData(testEntity);
        return entity;
    }

}
