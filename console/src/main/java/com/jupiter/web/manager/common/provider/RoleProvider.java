package com.jupiter.web.manager.common.provider;

import com.jupiter.web.manager.bus.admin.dto.RoleDto;
import org.apache.ibatis.jdbc.SQL;

public class RoleProvider {

    public String search(RoleDto roleDto) {
        SQL sql = new SQL() {
            {
                SELECT("*");
                FROM("role");
            }
        };
        if (roleDto.getRoleName() != null) {
            sql.WHERE("role_name like concat('%',#{roleName},'%')");
        }
        return sql.toString();
    }
}
