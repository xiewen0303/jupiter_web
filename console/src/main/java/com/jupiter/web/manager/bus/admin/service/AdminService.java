package com.jupiter.web.manager.bus.admin.service;

import com.github.pagehelper.PageHelper;
import com.jupiter.web.manager.bus.admin.dto.AdminDto;
import com.jupiter.web.manager.common.entity.Admin;
import com.jupiter.web.manager.utils.VerifyCodeUtils;
import com.jupiter.web.manager.common.dao.AdminDao;
import com.jupiter.web.manager.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 后台管理员设置接口实现
 */
@Service
public class AdminService {

    @Autowired
    private AdminDao adminDao;

    public List<Admin> getAdminPageList(AdminDto adminDto) {
        if (!StringUtils.isEmpty(adminDto.getPage()) && !StringUtils.isEmpty(adminDto.getRows())) {
            PageHelper.startPage(adminDto.getPage(), adminDto.getRows());
        }

        List<Admin> adminInfoList = adminDao.search(adminDto);
        return adminInfoList;
    }

    /**
     * 查询管理员
     *
     * @param admin
     */
    public Admin getAdminByProperty(Admin admin) {
        return adminDao.getOne(admin);
    }

    /**
     * 根据主键id查询管理员
     */
    public Admin getAdminById(Long id) {
        return adminDao.getById(id);
    }

    public boolean insertAdmin(Admin admin) throws Exception {
        //使用图片验证码的随机方法生成随机盐值
        String salt = VerifyCodeUtils.generateVerifyCode(6);
        admin.setSalt(salt);
        //将密码加密并使用盐值
        String passwd = Utils.encodeMD5(admin.getPassword().concat(salt));
        admin.setPassword(passwd);
        return adminDao.insert(admin) > 0;
    }

    /**
     * 修改管理员信息
     *
     * @param admin
     */
    public boolean updateAdminInfo(Admin admin) {
        return adminDao.update(admin) > 0;
    }


    /**
     * 检查管理员名是否已存在
     *
     * @param adminName
     * @return
     */
    public boolean checkIsNameExist(String adminName) {
        Admin adminCondition = new Admin();
        adminCondition.setAdminName(adminName);
        Admin adminInfo = adminDao.getOne(adminCondition);

        if (null == adminInfo) {
            return false;
        } else {
            return true;
        }
    }


    /**
     * 验证修改管理员的名字是否存在
     *
     * @param adminVo
     * @return
     */
    public boolean checkIsUpdateNameExist(Admin adminVo) {
        Admin adminCondition = new Admin();
        adminCondition.setAdminName(adminVo.getAdminName());
        Admin adminInfo = adminDao.getFirstOne(adminCondition);

        if (null == adminInfo) {
            return false;
        } else if (adminInfo.getId().compareTo(adminVo.getId()) == 0) {
            //修改时,当查询出的结果是自己本身时,允许修改
            return false;
        } else {
            return true;
        }
    }

    /**
     * 检查旧密码是否匹配
     *
     * @param id
     * @param oldPassword
     * @return
     */
    public boolean checkOldPasswdMatch(Long id, String oldPassword) throws Exception {
        Admin oldAdmin = adminDao.getById(id);

        Admin admin = new Admin();
        admin.setId(id);
        admin.setPassword(Utils.encodeMD5(oldPassword.concat(oldAdmin.getSalt())));
        admin = adminDao.getOne(admin);
        return admin != null;
    }

}
