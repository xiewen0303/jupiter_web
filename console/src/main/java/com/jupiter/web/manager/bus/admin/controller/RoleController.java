package com.jupiter.web.manager.bus.admin.controller;

import com.github.pagehelper.PageInfo;
import com.jupiter.web.manager.bus.admin.dto.CommonResponse;
import com.jupiter.web.manager.bus.admin.service.RoleService;
import com.jupiter.web.manager.common.entity.Role;
import com.jupiter.web.manager.log.LogAno;
import com.jupiter.web.manager.log.LogType;
import com.jupiter.web.manager.bus.admin.dto.RoleDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("role")
public class RoleController extends BaseController {
    private static Logger logger = LoggerFactory.getLogger(RoleController.class);

    @Autowired
    private RoleService roleService;

    @RequestMapping("/roleListPage")
    public String adminListPage() {
        return "role/roleList";
    }

    @ResponseBody
    @RequestMapping("/roleList")
    public CommonResponse<PageInfo<Role>> roleList(RoleDto roleDto) {
        CommonResponse<PageInfo<Role>> result = new CommonResponse<>();

        List<Role> list = roleService.getRolePageList(roleDto);

        PageInfo<Role> pageInfo = new PageInfo<>(list);

        result.setModule(pageInfo);
        result.setCode("200");
        result.setMsg("查询成功");

        return result;
    }

    @ResponseBody
    @RequestMapping("/saveRole")
    @LogAno(logType = LogType.ROLE_ADD)
    public CommonResponse saveRole(Role role) {
        CommonResponse result = new CommonResponse();
        try {
            Long roleId = roleService.insertRole(role);

            if (roleId != null && roleId > 0) {
                result.setCode("200");
                result.setMsg("新增角色成功");
            } else {
                result.setCode("300");
                result.setMsg("新增角色失败");
            }
        } catch (Exception e) {
            logger.error("insert role error.", e);
            result.setCode("500");
            result.setMsg("新增角色异常");
        }

        return result;
    }

    @ResponseBody
    @RequestMapping("roleEdit/init/{id}")
    public CommonResponse<Role> initRoleEdit(@PathVariable("id") final Long id) throws Exception {
        CommonResponse<Role> result = new CommonResponse<>();
        Role Role = roleService.getRoleById(id);

        result.setModule(Role);
        result.setCode("200");
        result.setMsg("初始化修改角色成功");

        return result;
    }

    @ResponseBody
    @RequestMapping("/updateRole")
    @LogAno(logType = LogType.ROLE_EDIT)
    public CommonResponse updateRole(Role role) {
        CommonResponse result = new CommonResponse();
        try {
            Role oldRole = roleService.getRoleById(role.getId());
            if (!oldRole.getRoleName().equals(role.getRoleName()) || !oldRole.getDescription().equals(role.getDescription())) {
                boolean flag = roleService.updateRole(role);

                if (flag) {
                    result.setCode("200");
                    result.setMsg("修改角色成功");
                } else {
                    result.setCode("300");
                    result.setMsg("修改角色失败");
                }

                return result;
            }

            result.setCode("200");
            result.setMsg("修改角色成功");
        } catch (Exception e) {
            logger.error("update role error.", e);
            result.setCode("500");
            result.setMsg("修改角色异常");
        }

        return result;
    }
}
