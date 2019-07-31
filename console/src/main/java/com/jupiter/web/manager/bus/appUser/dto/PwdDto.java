package com.jupiter.web.manager.bus.appUser.dto;

import lombok.Data;

@Data
public class PwdDto {

    private String sid;
    private String mobile;
    private String captcha;
    private String password;
    private String old_password;


}
