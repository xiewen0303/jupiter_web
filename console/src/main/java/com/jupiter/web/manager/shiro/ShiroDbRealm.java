package com.jupiter.web.manager.shiro;

import com.jupiter.web.manager.bus.admin.service.AdminService;
import com.jupiter.web.manager.bus.admin.service.PermissionService;
import com.jupiter.web.manager.bus.admin.service.RoleService;
import com.jupiter.web.manager.common.entity.Admin;
import com.jupiter.web.manager.common.entity.Role;
import com.jupiter.web.manager.common.entity.Permission;
import com.jupiter.web.interceptor.PrivilegeInteceptor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ShiroDbRealm extends AuthorizingRealm {

    @Autowired
    private AdminService adminService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private PermissionService securityManageService;

    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        Session session = SecurityUtils.getSubject().getSession();
        Admin admin = (Admin) session.getAttribute("login_admin");
        if(admin == null){
            log.error("no admin infos,sessionId={}",session.getId());
            return null;
        }
        if ( admin.getInfo() != null) {
            return admin.getInfo();
        }
        // 获取当前登录的用户名
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        List<String> permissions = new ArrayList<>();
        if (admin.getRoleId() != null) {
            // 查找角色
            Role role = roleService.getRoleById(admin.getRoleId());
            // 给当前用户设置角色
            info.addRole(role.getRoleName());
            List<Permission> permissionList = securityManageService.findPermissionsByRoleId(role.getId());
            for (Permission rolePermission : permissionList) {
                permissions.add(rolePermission.getUrl());
            }
        }
        // 给当前用户设置权限
        info.addStringPermissions(permissions);
        session.setTimeout(3600000);
        admin.setInfo(info);
        return info;
    }

    /*** 认证回调函数,登录时调用.*/
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws
            AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
        Admin condition = new Admin();
        condition.setAdminName(token.getUsername());
        condition.setPassword(new String(token.getPassword()));
        condition.setForbid(false);
        Admin adminInfo;
        try {
            adminInfo = this.adminService.getAdminByProperty(condition);
        } catch (Exception e) {
            e.printStackTrace();
            throw new AuthenticationException("登录异常！");
        }
        if (adminInfo != null) {
            if (adminInfo.getForbid()) {
                throw new AuthenticationException("管理员已禁用！");
            }

            SecurityUtils.getSubject().getSession().setTimeout(3600000);
            log.error("login set error sersionId={}",SecurityUtils.getSubject().getSession().getId());
            SecurityUtils.getSubject().getSession().setAttribute("login_admin", adminInfo);
            PrivilegeInteceptor.setLogin(SecurityUtils.getSubject().getSession().getId().toString(), adminInfo.getId());
            return new SimpleAuthenticationInfo(adminInfo, adminInfo.getPassword(), adminInfo.getAdminName());
        } else {
            throw new AuthenticationException("错误的用户名或密码！");
        }
    }
}
