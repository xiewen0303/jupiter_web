package com.jupiter.web.manager.common.dao;

import com.tron.common.mysql.mybatis.dao.CommonDao;
import com.jupiter.web.manager.common.entity.Role;
import com.jupiter.web.manager.common.provider.RoleProvider;
import com.jupiter.web.manager.bus.admin.dto.RoleDto;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleDao extends CommonDao<Role, Long> {

    @Override
    default Class<Role> getSelfClass() {
        return Role.class;
    }

    @SelectProvider(type = RoleProvider.class, method = "search")
    List<Role> search(RoleDto roleDto);

    @Select("SELECT role_name FROM role WHERE id=#{roleId}")
    String getRoleNameByRoleId(Long roleId);

}
