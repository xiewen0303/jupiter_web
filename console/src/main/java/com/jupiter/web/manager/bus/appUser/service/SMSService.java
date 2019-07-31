package com.jupiter.web.manager.bus.appUser.service;

import com.alibaba.fastjson.JSONObject;
import com.jupiter.web.interceptor.AppCofig;
import com.jupiter.web.manager.common.entity.SkyLineSendLog;
import com.jupiter.web.manager.utils.DateUtil;
import com.jupiter.web.manager.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Slf4j
@Service
public class SMSService {

    @Autowired
    private AppCofig appCofig;

    public void sendSMS(String numbers, String content, int type) {
        SkyLineSendLog skyLineSendLog = new SkyLineSendLog();
        skyLineSendLog.setNumbers(numbers);
        skyLineSendLog.setContent(content);


        // 调用第三方接口
        String version = "1.0";
        String account;
        String pwd;
        if (type == 1) {
            account = appCofig.getSkyline_valicode_api_account();
            pwd = appCofig.getSkyline_valicode_api_pwd();
        } else {
            account = appCofig.getSkyline_other_api_account();
            pwd = appCofig.getSkyline_other_api_pwd();
        }


        String timestamp_str = DateUtil.formatDate(new Date(), "yyyyMMddHHmmss");// 20190730150516
        String wait_sign = account + pwd + timestamp_str;
        String sign = Utils.encodeMD5(wait_sign);

        JSONObject params = new JSONObject();
        params.put("version", version);
        params.put("numbers", numbers);
        params.put("senderid", "Demoos");
        params.put("content", content);

    }
}
