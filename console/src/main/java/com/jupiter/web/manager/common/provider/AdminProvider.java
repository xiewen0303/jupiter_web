package com.jupiter.web.manager.common.provider;

import com.jupiter.web.manager.bus.admin.dto.AdminDto;
import org.apache.ibatis.jdbc.SQL;

public class AdminProvider {

    public String search(AdminDto adminDto) {
        SQL sql = new SQL() {
            {
                SELECT("*");
                FROM("admin");
            }
        };
        if (adminDto.getId() != null) {
            sql.WHERE("id = #{id}");
        }
        if (adminDto.getForbid() != null && adminDto.getForbid() >= 0) {
            sql.WHERE("forbid = #{forbid}");
        }
        if (adminDto.getAdminName() != null) {
            sql.WHERE("admin_name like concat('%',#{adminName},'%')");
        }
        return sql.toString();
    }
}
