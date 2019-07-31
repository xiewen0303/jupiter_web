package com.jupiter.web.manager.common.provider;

import com.jupiter.web.manager.common.entity.RolePermission;

import java.util.List;
import java.util.Map;

public class RolePermissionProvider {

    public String insertBatch(Map map) {
        List<RolePermission> list = (List<RolePermission>) map.get("list");
        StringBuilder sb = new StringBuilder();

        sb.append("INSERT INTO role_permission (permission_id,role_id) VALUES ");
        for (int i = 0; i < list.size(); i++) {
            RolePermission rolePermission = list.get(i);
            sb.append("(").append(rolePermission.getPermissionId()).append(",").append(rolePermission.getRoleId());
            if (i == list.size() - 1) {
                sb.append(");");
            } else {
                sb.append("),");
            }
        }
        return sb.toString();
    }

}
