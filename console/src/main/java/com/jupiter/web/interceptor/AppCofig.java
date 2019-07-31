package com.jupiter.web.interceptor;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Data
@Component
@ConfigurationProperties(prefix = "app-config")
public class AppCofig {

    private Map<String, String> session_url;

    private int session_valid_time;
    private int captcha_valid_time;
    private String skyline_valicode_api_account;
    private String skyline_valicode_api_pwd;
    private String skyline_other_api_account;
    private String skyline_other_api_pwd;
    private String allow_origin;
}
