package com.jupiter.web.manager.common.entity;

import com.jupiter.web.manager.bus.appUser.dto.UserDto;
import com.tron.common.mysql.mybatis.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Table;

/**
 * Created by zhangxiqiang on 2019/7/23.
 */
@Data
@Table(name = "jpt_user")
public class User extends BaseEntity<String> {
    private String mobile;
    private String nickname;
    private String password;
    private String salt;
    private String avatarFileUrl;
    private String avatarFileId;
    private String province;
    private String city;
    private Long createTime;
    private Integer writeOffFlag;
    private Long writeOffTime;
    private String qq;
    private String email;
    private String inviteCode;
    private String invitedBy;
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
    private String remark;
    private String addUser;
    private String updateUser;

    public void loadByDto(UserDto user, String ip) {
        this.mobile = user.getMobile();
        this.nickname = user.getNickname();
        this.province = user.getProvince();
        this.city = user.getCity();
        this.qq = user.getQq();
        this.email = user.getEmail();
        this.remark = user.getRemark();
        this.inviteCode = user.getInvite_code();
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
