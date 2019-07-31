package com.jupiter.web.controller;

import lombok.Data;

import java.io.Serializable;

@Data
public class TestEntity implements Serializable {

    private String add_product;

    private String imei;

    private String mobile;

    private String idfa;

    private int loc_lng;

    private String password;

    private String device_model;

    private String device_platform;

    private String category_id;

    private String add_channel;

}
