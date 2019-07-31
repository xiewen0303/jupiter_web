package com.jupiter.web.manager.common.dao;

import com.jupiter.web.manager.bus.admin.dto.AdminDto;
import com.jupiter.web.manager.common.entity.Admin;
import com.jupiter.web.manager.common.provider.AdminProvider;
import com.tron.common.mysql.mybatis.dao.CommonDao;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminDao extends CommonDao<Admin, Long> {

    @Override
    default Class<Admin> getSelfClass() {
        return Admin.class;
    }

    @SelectProvider(type = AdminProvider.class, method = "search")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "roleId", column = "role_id"),
            @Result(property = "roleName", column = "role_name",
                    one = @One(select = "RoleDao.getRoleNameByRoleId"))
    })
    List<Admin> search(AdminDto adminDto);

}
