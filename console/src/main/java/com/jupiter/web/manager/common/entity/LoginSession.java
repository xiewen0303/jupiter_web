package com.jupiter.web.manager.common.entity;

import com.tron.common.mysql.mybatis.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Table;

/**
 * Created by zhangxiqiang on 2019/7/23.
 */
@Data
@Table(name = "jpt_login_session")
public class LoginSession extends BaseEntity<String> {

    private String sid;
    private String userId;
    private String addProduct;
    private String addChannel;
    private Long lastActionTime;
    private String addUser;
    private String updateUser;

}
