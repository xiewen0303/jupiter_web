package com.jupiter.web.controller;

import com.tron.framework.dto.ResponseEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/jupiter/api/web")
public class TestController {

    @RequestMapping("/testParam")
    public ResponseEntity testParam(TestEntity testEntity) {
        log.debug("testEntity-------------- " + testEntity);
        ResponseEntity entity = new ResponseEntity();
        entity.setMsg("test啦啦啦");
        entity.setStatus(1);
        testEntity.setImei("测试哦");
        entity.setData(testEntity);
        log.debug("returnEntity-------------- " + entity);
        return entity;
    }

    @RequestMapping("/testGetParam")
    public ResponseEntity testGetParam(TestEntity testEntity) {
        log.debug("testGetParam-------------- " + testEntity);
        ResponseEntity entity = new ResponseEntity();
        entity.setMsg("test啦 get 啦啦");
        entity.setStatus(1);
        testEntity.setImei("测试哦 get");
        entity.setData(testEntity);
        log.debug("returnGetEntity-------------- " + entity);
        return entity;
    }
}
