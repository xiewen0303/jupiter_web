package com.jupiter.web.manager.common.dao;

import com.tron.common.mysql.mybatis.dao.CommonDao;
import com.jupiter.web.manager.common.provider.PermissionProvider;
import com.jupiter.web.manager.common.entity.Permission;
import com.jupiter.web.manager.bus.admin.dto.PermissionDto;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PermissionDao extends CommonDao<Permission, Long> {

    @Override
    default Class<Permission> getSelfClass() {
        return Permission.class;
    }

    @Select("SELECT * FROM permission WHERE EXISTS (SELECT 1 FROM role_permission WHERE role_permission.permission_id = permission.id AND role_permission.role_id = #{roleId})")
    List<Permission> findPermissionsByRoleId(Long roleId);

    @Select("SELECT ID FROM permission WHERE EXISTS (SELECT 1 FROM role_permission WHERE role_permission.permission_id = permission.id AND role_permission.role_id = #{roleId})")
    List<Long> findPermissionIdsByRoleId(Long roleId);

    @SelectProvider(type = PermissionProvider.class, method = "search")
    List<Permission> search(PermissionDto permissionDto);

}
