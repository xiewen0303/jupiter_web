package com.jupiter.web.manager.bus.admin.controller;

import com.github.pagehelper.PageInfo;
import com.jupiter.web.manager.bus.admin.dto.AdminDto;
import com.jupiter.web.manager.bus.admin.dto.CommonResponse;
import com.jupiter.web.manager.bus.admin.service.AdminService;
import com.jupiter.web.manager.bus.admin.service.RoleService;
import com.jupiter.web.manager.common.entity.Admin;
import com.jupiter.web.manager.common.entity.Role;
import com.jupiter.web.manager.log.LogAno;
import com.jupiter.web.manager.log.LogType;
import com.jupiter.web.manager.utils.VerifyCodeUtils;
import com.jupiter.web.interceptor.PrivilegeInteceptor;
import com.jupiter.web.manager.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("admin")
public class AdminController extends BaseController {
    private static Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    private AdminService adminService;

    @Autowired
    private RoleService roleService;

    @RequestMapping("/adminListPage")
    public String adminListPage(Model model) {
        // 页面参数
        Map<Integer, String> typeMap = new HashMap<>();
        typeMap.put(-1, "全部");
        typeMap.put(0, "正常");
        typeMap.put(1, "禁用");
        model.addAttribute("typeMap", typeMap);
        //角色列表
        List<Role> roleList = roleService.getAll();
        model.addAttribute("roleList", roleList);

        return "admin/adminList";
    }

    @ResponseBody
    @RequestMapping("/adminList")
    public CommonResponse<PageInfo<Admin>> adminList(AdminDto adminDto) {
        CommonResponse<PageInfo<Admin>> result = new CommonResponse<>();

        try {
            List<Admin> list = adminService.getAdminPageList(adminDto);
            PageInfo<Admin> pageInfo = new PageInfo<>(list);

            result.setModule(pageInfo);
            result.setCode("200");
            result.setMsg("分页查询管理员列表成功");
        } catch (Exception e) {
            logger.error("分页查询管理员列表异常", e);
            result.setCode("500");
            result.setMsg("分页查询管理员列表异常");
        }

        return result;
    }

    @ResponseBody
    @RequestMapping("/saveAdmin")
    @LogAno(logType = LogType.ADMIN_ADD)
    public CommonResponse<?> saveAdmin(Admin admin) throws Exception {
        CommonResponse<?> result = new CommonResponse<>();
        try {
            boolean isNameExist = adminService.checkIsNameExist(admin.getAdminName());
            if (isNameExist) {
                result.setCode("300");
                result.setMsg("管理员名已存在,添加管理员失败");
                return result;
            }
            Admin newAdmin = new Admin();
            BeanUtils.copyProperties(admin, newAdmin);

            boolean flag = adminService.insertAdmin(newAdmin);
            admin.setPassword(null);

            if (flag) {
                result.setCode("200");
                result.setMsg("添加管理员成功");
            } else {
                result.setCode("300");
                result.setMsg("添加管理员失败");
            }
        } catch (Exception e) {
            logger.error("add admin error.", e);
            result.setCode("500");
            result.setMsg("系统异常,添加管理员失败,请联系管理员");
            throw new Exception();
        }

        return result;
    }

    @ResponseBody
    @RequestMapping("/info/init/{id}")
    public CommonResponse<Admin> initEditAdmin(@PathVariable("id") final Long id) throws Exception {
        CommonResponse<Admin> result = new CommonResponse<>();
        Admin adminVo = adminService.getAdminById(id);

        result.setModule(adminVo);
        result.setCode("200");
        result.setMsg("初始化修改管理员成功");

        return result;
    }


    /**
     * 修改管理员信息
     */
    @ResponseBody
    @RequestMapping("/updateAdmin")
    @LogAno(logType = LogType.ADMIN_EDIT)
    public CommonResponse<?> updateAdmin(Admin adminVo) throws Exception {
        CommonResponse<?> result = new CommonResponse<>();
        try {
            boolean isNameExist = adminService.checkIsUpdateNameExist(adminVo);
            if (isNameExist) {
                result.setCode("300");
                result.setMsg("管理员名已存在,修改管理员失败");
                return result;
            }

            Admin admin = new Admin();
            BeanUtils.copyProperties(adminVo, admin);

            boolean flag = adminService.updateAdminInfo(admin);
            if (flag) {
                result.setCode("200");
                result.setMsg("修改管理员信息成功");
            } else {
                result.setCode("300");
                result.setMsg("修改管理员信息失败");
            }

        } catch (Exception e) {
            logger.error("update admin error.", e);
            result.setCode("500");
            result.setMsg("修改管理员信息异常");
        }

        return result;
    }

    /**
     * 修改管理员密码
     */
    @ResponseBody
    @RequestMapping("/updateAdminPwd")
    @LogAno(logType = LogType.ADMIN_PWD)
    public CommonResponse<?> updateAdminPwd(Admin admin) throws Exception {
        CommonResponse<?> result = new CommonResponse<>();
        try {
            //使用图片验证码的随机方法生成随机盐值
            String salt = VerifyCodeUtils.generateVerifyCode(6);
            admin.setSalt(salt);
            //将密码加密并使用盐值
            String passwd = Utils.encodeMD5(admin.getPassword().concat(salt));
            admin.setPassword(passwd);
            boolean flag = adminService.updateAdminInfo(admin);
            admin.setPassword(null);
            admin.setSalt(null);

            if (flag) {
                PrivilegeInteceptor.setLogout(admin.getId());
                result.setCode("200");
                result.setMsg("修改管理员密码成功");
            } else {
                result.setCode("300");
                result.setMsg("修改管理员密码失败");
            }

        } catch (Exception e) {
            logger.error("update passwd error.", e);
            result.setCode("500");
            result.setMsg("修改管理员密码异常");
        }

        return result;
    }


    /**
     * 禁用管理员
     */
    @ResponseBody
    @RequestMapping("/forbidAdmin/{id}")
    @LogAno(logType = LogType.ADMIN_FORBID)
    public CommonResponse forbidAdmin(@PathVariable("id") final Long id) throws Exception {
        CommonResponse result = new CommonResponse<>();
        try {
            Admin admin = new Admin();
            admin.setId(id);
            admin.setForbid(true);
            boolean flag = adminService.updateAdminInfo(admin);

            if (flag) {
//                PrivilegeInteceptor.setLogout(id);
                result.setCode("200");
                result.setMsg("禁用管理员成功");
            } else {
                result.setCode("300");
                result.setMsg("禁用管理员失败");
            }

        } catch (Exception e) {
            logger.error("forbid admin error.", e);
            result.setCode("500");
            result.setMsg("禁用管理员异常");
        }

        return result;
    }

    /**
     * 启用管理员
     */
    @ResponseBody
    @RequestMapping("/enableAdmin/{id}")
    @LogAno(logType = LogType.ADMIN_ENABLE)
    public CommonResponse enableAdmin(@PathVariable("id") final Long id) throws Exception {
        CommonResponse result = new CommonResponse<>();
        try {
            Admin admin = new Admin();
            admin.setId(id);
            admin.setForbid(false);
            boolean flag = adminService.updateAdminInfo(admin);

            if (flag) {
                result.setCode("200");
                result.setMsg("启用管理员成功");
            } else {
                result.setCode("300");
                result.setMsg("启用管理员失败");
            }

        } catch (Exception e) {
            logger.error("enable admin error.", e);
            result.setCode("500");
            result.setMsg("禁用管理员异常");
        }

        return result;
    }

    @RequestMapping("/initLocalPwdUpdate")
    public String initLocalPwdUpdate(Model model) {
        Admin adminInfo = (Admin) sessionContextUtils.getContextRequest().getSession().getAttribute("login_admin");
        // 页面参数
        model.addAttribute("id", adminInfo.getId());

        return "admin/localPwdUpdatePage";
    }

    /**
     * 修改管理员密码
     */
    @ResponseBody
    @RequestMapping("/updateLocalPwd")
    @LogAno(logType = LogType.CHANGE_PWD, isPrivateLog = true)
    public CommonResponse updateLocalPwd(Long id, String oldPassword, String newPassword) throws Exception {
        CommonResponse result = new CommonResponse<>();
        try {
            boolean oldPasswdMatch = adminService.checkOldPasswdMatch(id, oldPassword);

            if (!oldPasswdMatch) {
                result.setCode("300");
                result.setMsg("旧密码不匹配,修改管理员密码失败");
                return result;
            }

            Admin admin = new Admin();
            admin.setId(id);
            //使用图片验证码的随机方法生成随机盐值
            String salt = VerifyCodeUtils.generateVerifyCode(6);
            admin.setSalt(salt);
            //将密码加密并使用盐值
            String passwd = Utils.encodeMD5(newPassword.concat(salt));
            admin.setPassword(passwd);
            boolean flag = adminService.updateAdminInfo(admin);

            if (flag) {
                result.setCode("200");
                result.setMsg("修改管理员密码成功");
            } else {
                result.setCode("300");
                result.setMsg("修改管理员密码失败");
            }

        } catch (Exception e) {
            logger.error("update passwd error.", e);
            result.setCode("500");
            result.setMsg("修改管理员密码异常");
        }

        return result;
    }

}
