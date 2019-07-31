package com.jupiter.web.manager.bus.admin.service;


import com.github.pagehelper.PageHelper;
import com.jupiter.web.manager.bus.admin.dto.PermissionTree;
import com.jupiter.web.manager.common.dao.PermissionDao;
import com.jupiter.web.manager.common.dao.RolePermissionDao;
import com.jupiter.web.manager.common.entity.Permission;
import com.jupiter.web.manager.common.entity.RolePermission;
import com.jupiter.web.manager.bus.admin.dto.PermissionDto;
import com.jupiter.web.manager.bus.admin.dto.RolePermissionDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class PermissionService {

    @Autowired
    private PermissionDao permissionDao;

    @Autowired
    private RolePermissionDao rolePermissionDao;

    /**
     * 根据角色ID查询角色权限
     *
     * @param roleId
     */
    public List<Permission> findPermissionsByRoleId(Long roleId) {
        return permissionDao.findPermissionsByRoleId(roleId);
    }

    /**
     * 查询所有权限
     */

    public List<Permission> getAllPermission() {
        return permissionDao.getAll();
    }


    /**
     * 查询当前权限节点的下级节点
     *
     * @param permissionDto
     */
    public List<Permission> getPermissionPageList(PermissionDto permissionDto) {
        if (!StringUtils.isEmpty(permissionDto.getPage()) && !StringUtils.isEmpty(permissionDto.getRows())) {
            PageHelper.startPage(permissionDto.getPage(), permissionDto.getRows());
        }
        List<Permission> permissionList = permissionDao.search(permissionDto);

        return permissionList;
    }

    /**
     * 新增权限
     *
     * @param permission
     */

    public boolean insert(Permission permission) {
        return permissionDao.insert(permission) > 0;
    }

    /**
     * 删除权限
     *
     * @param permissionId
     */

    @Transactional
    public boolean delete(Long permissionId) {
        rolePermissionDao.deleteByPermissionId(permissionId);
        return permissionDao.delete(permissionId) > 0;
    }

    /**
     * 修改权限
     *
     * @param permission
     */

    public boolean update(Permission permission) {
        return permissionDao.update(permission) > 0;
    }

    /**
     * 查询权限
     *
     * @param id
     */

    public Permission getPermissionById(Long id) {
        return permissionDao.getById(id);
    }

    /**
     * 根据roleId查询权限列表
     *
     * @param roleId
     * @return
     */

    public List<PermissionTree> getPermissionTreeData(Long roleId) {
        List<PermissionTree> result = new ArrayList<>();

        List<Permission> totalTreeList = permissionDao.getAll();
        List<Long> permissionIds = permissionDao.findPermissionIdsByRoleId(roleId);

        for (Permission permission : totalTreeList) {
            PermissionTree permissionTree = new PermissionTree();
            permissionTree.setId(permission.getId());
            permissionTree.setParent(permission.getParentId() == 0 ? "#" : permission.getParentId() + "");
            permissionTree.setText(permission.getPermissionName());
            permissionTree.setSort(permission.getPriority());

            if (checkIsExistChildNode(totalTreeList, permission.getId())) {
                permissionTree.setOpened(true);
            }
            if (permissionIds.contains(permissionTree.getId())) {
                permissionTree.setSelected(true);
            }
            result.add(permissionTree);
        }

        Collections.sort(result, new Comparator<PermissionTree>() {
            public int compare(PermissionTree o1, PermissionTree o2) {
                return o1.getSort().compareTo(o2.getSort());
            }
        });

        return result;
    }

    @Transactional
    public void authorize(RolePermissionDto rolePermissionDto) {
        //清除原有的角色关联资源信息
        rolePermissionDao.deleteByRoleId(rolePermissionDto.getRoleId());

        //重新关联角色资源信息
        List<RolePermission> list = new ArrayList<>();
        for (Long securityId : rolePermissionDto.getPermissionList()) {
            RolePermission rolePermission = new RolePermission();
            rolePermission.setRoleId(rolePermissionDto.getRoleId());
            rolePermission.setPermissionId(securityId);
            list.add(rolePermission);
        }

        if (list.size() > 0) {
            rolePermissionDao.insertBatch(list);
        }
    }

    /**
     * 判断当前节点是(false)否(true)存在子节点
     *
     * @param permissionList
     * @param id
     * @return
     */
    private boolean checkIsExistChildNode(List<Permission> permissionList, Long id) {
        for (Permission permission : permissionList) {
            if (permission.getParentId().compareTo(id) == 0) {
                return true;
            }
        }
        return false;
    }
}
