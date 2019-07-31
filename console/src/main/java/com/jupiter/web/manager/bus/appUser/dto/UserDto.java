package com.jupiter.web.manager.bus.appUser.dto;

import lombok.Data;

@Data
public class UserDto {

    private String sid;
    private String mobile;
    private String captcha;
    private String password;
    private String nickname;

    private String province;
    private String city;

    private String qq;

    private String email;

    private String invite_code;

    private String remark;

    private String device_platform;

    private String imei;

    private String mac;

    private String idfa;

    private String cid;

    private String device_model;
    private String device_os;
    private String add_product;
    private String add_channel;
    private String market_channel;
    private String product_version;
    private String network_type;


}
