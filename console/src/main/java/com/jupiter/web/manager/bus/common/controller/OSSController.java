package com.jupiter.web.manager.bus.common.controller;

import com.jupiter.web.aop.LoginUser;
import com.jupiter.web.manager.bus.common.dto.OSSPolicyOutputDto;
import com.jupiter.web.manager.bus.common.dto.OSSTokenOutputDto;
import com.jupiter.web.manager.bus.common.service.OSService;
import com.jupiter.web.manager.common.entity.User;
import com.tron.framework.dto.ResponseEntity;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;

/**
 * @author fuyuling
 * @created 2019/7/19 11:31
 */
@Controller
@RequestMapping("/jupiter/api/web/oss")
public class OSSController {

    @Resource
    private OSService osService;

    @RequestMapping("/policy")
    @ResponseBody
    public ResponseEntity getPolicy(String dir) throws UnsupportedEncodingException {
        if (StringUtils.isBlank(dir)) {
            return new ResponseEntity(ResponseEntity.STATUS_FAIL, null, "无效的参数dir", null);
        }
        OSSPolicyOutputDto ossPolicy = osService.getOSSPolicy(dir);
        return new ResponseEntity(ResponseEntity.STATUS_OK, null, null, ossPolicy);
    }

    @RequestMapping("/token")
    @ResponseBody
    public OSSTokenOutputDto getToken(@LoginUser User user) {
        return osService.getOSSToken(user);
    }

}
