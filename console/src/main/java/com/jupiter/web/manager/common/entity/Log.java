package com.jupiter.web.manager.common.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tron.common.mysql.mybatis.entity.BaseEntity;
import com.jupiter.web.manager.log.LogType;
import com.jupiter.web.manager.utils.DateUtil;
import lombok.Data;

import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

@Data
@Table(name = "log")
@JsonIgnoreProperties(value = {"handler"})
public class Log extends BaseEntity<Long> {

    private Long adminId;

    private Integer logType;

    private String logUri;

    private String logInfo;

    private Long logTime;

    private String adminName;

    @Transient
    private String logTypeInfo;

    @Transient
    private String logShowTime;

    public void setLogType(Integer logType) {
        this.logType = logType;
        this.logTypeInfo = LogType.getDetailByType(logType);
    }

    public void setLogTime(Long logTime) {
        this.logTime = logTime;
        this.logShowTime = DateUtil.formatDate(new Date(logTime));
    }

}
