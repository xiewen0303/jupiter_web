package com.jupiter.web.manager.bus.admin.dto;

import lombok.Data;

@Data
public class LogDto extends BaseDto {

    private String adminName;

    private Integer logType;

    private Long logStartTime;

    private Long logEndTime;

    private String logStart;

    private String logEnd;

}
