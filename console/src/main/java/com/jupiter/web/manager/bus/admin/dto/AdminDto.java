package com.jupiter.web.manager.bus.admin.dto;

import lombok.Data;

@Data
public class AdminDto extends BaseDto {

    //主键id
    private Long id;
    //登录名
    private String adminName;
    private Integer forbid;
}
