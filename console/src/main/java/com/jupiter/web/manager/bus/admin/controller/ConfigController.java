package com.jupiter.web.manager.bus.admin.controller;

import com.jupiter.web.manager.bus.admin.service.ConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Slf4j
@Controller
@RequestMapping("config")
public class ConfigController {

    @Resource
    private ConfigService configService;

    @ResponseBody
    @RequestMapping("/reloadConfig")
    public String reloadConfig(@RequestParam(required = false) String pwd) {
        try {
            //TODO  可以验证密码，
            configService.reloadConfig();
        } catch (Exception e) {
            log.error("刷新数据操作异常,error:{}",e.getMessage());
            return "FAIL";
        }
        return "SUCCESS";
    }
}
