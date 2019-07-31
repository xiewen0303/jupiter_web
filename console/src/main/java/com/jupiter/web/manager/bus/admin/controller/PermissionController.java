package com.jupiter.web.manager.bus.admin.controller;

import com.github.pagehelper.PageInfo;
import com.jupiter.web.manager.bus.admin.dto.CommonResponse;
import com.jupiter.web.manager.bus.admin.dto.PermissionDto;
import com.jupiter.web.manager.bus.admin.dto.PermissionTree;
import com.jupiter.web.manager.bus.admin.dto.RolePermissionDto;
import com.jupiter.web.manager.bus.admin.service.PermissionService;
import com.jupiter.web.manager.common.dao.AdminDao;
import com.jupiter.web.manager.common.entity.Admin;
import com.jupiter.web.manager.common.entity.Permission;
import com.jupiter.web.manager.log.LogAno;
import com.jupiter.web.manager.log.LogType;
import com.jupiter.web.interceptor.PrivilegeInteceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("permission")
public class PermissionController extends BaseController {
    private static Logger logger = LoggerFactory.getLogger(PermissionController.class);

    @Autowired
    private PermissionService permissionService;
    @Autowired
    private AdminDao adminDao;

    /**
     * 权限分页列表
     */
    @RequestMapping("/permissionListPage")
    public String loginPage(Model model) throws Exception {
        List<Permission> permissionList = permissionService.getAllPermission();
        model.addAttribute("permissionList", permissionList);

        return "permission/permissionList";
    }

    @ResponseBody
    @RequestMapping("/permissionList")
    public CommonResponse<PageInfo<Permission>> spermissionList(PermissionDto permissionDto) throws Exception {
        CommonResponse<PageInfo<Permission>> result = new CommonResponse<>();

        List<Permission> list = permissionService.getPermissionPageList(permissionDto);

        PageInfo<Permission> pageInfo = new PageInfo<>(list);
        result.setModule(pageInfo);
        result.setCode("200");
        result.setMsg("查询成功");

        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/permissionTree/{roleId}")
    public CommonResponse permissionTree(@PathVariable("roleId") final Long roleId) {
        CommonResponse result = new CommonResponse<>();
        List<PermissionTree> list = permissionService.getPermissionTreeData(roleId);
        result.setModule(list);
        result.setCode("200");
        result.setMsg("查询成功");
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/permission/authorize")
    @LogAno(logType = LogType.ROLE_AUTHORIZE)
    public CommonResponse authorize(@RequestBody RolePermissionDto rolePermissionDto) {
        CommonResponse result = new CommonResponse<>();
        try {
            permissionService.authorize(rolePermissionDto);

            //判断关联账户,全部登出
            Admin admin = new Admin();
            admin.setRoleId(rolePermissionDto.getRoleId());
            List<Admin> adminList = adminDao.get(admin);
            if (adminList != null && !adminList.isEmpty()) {
                adminList.forEach(t -> PrivilegeInteceptor.setLogout(t.getId()));
            }

            result.setCode("200");
            result.setMsg("角色权限授权成功");
        } catch (Exception e) {
            logger.error("角色权限授权异常", e);
            result.setCode("500");
            result.setMsg("角色权限授权异常");
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/savePermission")
    @LogAno(logType = LogType.PERMISSION_ADD)
    public CommonResponse savePermission(Permission permission) {
        CommonResponse result = new CommonResponse<>();
        try {
            boolean flag = permissionService.insert(permission);
            if (flag) {
                result.setCode("200");
                result.setMsg("新增权限成功");
            } else {
                result.setCode("300");
                result.setMsg("新增权限失败");
            }

        } catch (Exception e) {
            logger.error("新增权限异常", e);
            result.setCode("500");
            result.setMsg("新增权限异常");
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "permissionEdit/init/{permissionId}")
    public CommonResponse initEditPermission(@PathVariable("permissionId") final Long permissionId) {
        CommonResponse result = new CommonResponse<>();

        Permission permission = permissionService.getPermissionById(permissionId);

        result.setModule(permission);
        result.setCode("200");
        result.setMsg("初始化权限信息成功");
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/updatePermission")
    @LogAno(logType = LogType.PERMISSION_EDIT)
    public CommonResponse updatePermission(Permission permission) {
        CommonResponse result = new CommonResponse();
        try {
            boolean flag = permissionService.update(permission);
            if (flag) {
                result.setCode("200");
                result.setMsg("修改权限信息成功");
            } else {
                result.setCode("300");
                result.setMsg("修改权限信息失败");
            }
        } catch (Exception e) {
            logger.error("修改权限信息异常", e);
            result.setCode("500");
            result.setMsg("修改权限信息异常");
        }

        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/deletePermission/{permissionId}")
    @LogAno(logType = LogType.PERMISSION_DELETE)
    public CommonResponse deletePermission(@PathVariable("permissionId") final Long permissionId) {
        CommonResponse result = new CommonResponse();

        boolean flag = permissionService.delete(permissionId);

        if (flag) {
            result.setCode("200");
            result.setMsg("删除权限成功");
        } else {
            result.setCode("300");
            result.setMsg("删除权限失败");
        }

        return result;
    }
}
