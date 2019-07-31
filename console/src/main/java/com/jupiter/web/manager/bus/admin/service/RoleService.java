package com.jupiter.web.manager.bus.admin.service;

import com.github.pagehelper.PageHelper;
import com.jupiter.web.manager.common.dao.RoleDao;
import com.jupiter.web.manager.common.entity.Role;
import com.jupiter.web.manager.bus.admin.dto.RoleDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class RoleService {

    @Autowired
    private RoleDao roleDao;

    /**
     * 分页查询角色
     */
    public List<Role> getRolePageList(RoleDto roleDto) {
        if (!StringUtils.isEmpty(roleDto.getPage()) && !StringUtils.isEmpty(roleDto.getRows())) {
            PageHelper.startPage(roleDto.getPage(), roleDto.getRows());
        }
        List<Role> roleList = roleDao.search(roleDto);
        return roleList;
    }

    public List<Role> getAll() {
        return roleDao.getAll();
    }


    /**
     * 新增角色
     *
     * @param role
     */

    public Long insertRole(Role role) {
        int i = roleDao.insert(role);
        return i > 0 ? role.getId() : null;
    }

    /**
     * 修改角色
     *
     * @param role
     */

    public boolean updateRole(Role role) {
        return roleDao.update(role) > 0;
    }

    /**
     * 根据ID查询角色
     *
     * @param id
     */

    public Role getRoleById(Long id) {
        return roleDao.getById(id);
    }
}
