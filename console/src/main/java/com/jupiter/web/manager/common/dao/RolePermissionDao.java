package com.jupiter.web.manager.common.dao;

import com.tron.common.mysql.mybatis.dao.CommonDao;
import com.jupiter.web.manager.common.provider.RolePermissionProvider;
import com.jupiter.web.manager.common.entity.RolePermission;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RolePermissionDao extends CommonDao<RolePermission, Long> {

    @Override
    default Class<RolePermission> getSelfClass() {
        return RolePermission.class;
    }

    @InsertProvider(type = RolePermissionProvider.class, method = "insertBatch")
    int insertBatch(@Param("list") List<RolePermission> list);

    @Delete("delete from role_permission where permission_id=#{permissionId}")
    void deleteByPermissionId(Long permissionId);

    @Delete("delete from role_permission where role_id=#{roleId}")
    void deleteByRoleId(Long roleId);

}
