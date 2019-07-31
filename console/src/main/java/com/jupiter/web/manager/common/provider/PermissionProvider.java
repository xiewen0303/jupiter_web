package com.jupiter.web.manager.common.provider;

import com.jupiter.web.manager.bus.admin.dto.PermissionDto;
import org.apache.ibatis.jdbc.SQL;

public class PermissionProvider {

    public String search(PermissionDto permissionDto) {
        SQL sql = new SQL() {
            {
                SELECT("*");
                FROM("permission");
            }
        };
        if (permissionDto.getId() != null) {
            sql.WHERE("id=#{id}");
        }
        if (permissionDto.getPermissionName() != null) {
            sql.WHERE("permission_name like concat('%',#{permissionName},'%')");
        }
        sql = sql.ORDER_BY("priority");
        return sql.toString();
    }


}
