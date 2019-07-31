package com.jupiter.web.manager.common.entity;

import com.jupiter.web.manager.bus.appUser.dto.UserDto;
import com.tron.common.mysql.mybatis.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Table;

@Data
@Table(name = "jpt_login_info")
public class LoginInfo extends BaseEntity<String> {
    private String mobile;
    private Integer result;
    private String devicePlatform;
    private String imei;
    private String mac;
    private String idfa;
    private String cid;
    private String ip;
    private String deviceModel;
    private String deviceOs;
    private String marketChannel;
    private String addProduct;
    private String addChannel;
    private String productVersion;
    private String networkType;
    private String addUser;
    private String updateUser;

    public void loadByUser(UserDto user, String ip) {
        this.mobile = user.getMobile();
        this.devicePlatform = user.getDevice_platform();
        this.imei = user.getImei();
        this.mac = user.getMac();
        this.idfa = user.getIdfa();
        this.cid = user.getCid();
        this.ip = ip;
        this.deviceModel = user.getDevice_model();
        this.deviceOs = user.getDevice_os();
        this.marketChannel = user.getMarket_channel();
        this.addProduct = user.getAdd_product();
        this.addChannel = user.getAdd_channel();
        this.productVersion = user.getProduct_version();
        this.networkType = user.getNetwork_type();
    }
}
