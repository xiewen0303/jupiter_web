package com.jupiter.web.manager.common.provider;

import com.jupiter.web.manager.bus.admin.dto.LogDto;
import org.apache.ibatis.jdbc.SQL;

public class LogProvider {

    public String search(LogDto logDto) {
        SQL sql = new SQL() {
            {
                SELECT("*");
                FROM("log");
            }
        };
        if (logDto.getLogType() != null) {
            sql.WHERE("log_type = #{logType}");
        }
        if (logDto.getLogStartTime() != null) {
            sql.WHERE("log_time >= #{logStartTime}");
        }
        if (logDto.getLogEndTime() != null) {
            sql.WHERE("log_time <= #{logEndTime}");
        }
        if (logDto.getAdminName() != null) {
            sql.WHERE("admin_name like concat('%',#{adminName},'%')");
        }
        return sql.toString();
    }
}
