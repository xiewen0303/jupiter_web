package com.jupiter.web.manager.bus.appUser.dto;

import lombok.Data;

@Data
public class CaptchaDto {

    private String mobile;
    private String type;
    private String send_type;

}
