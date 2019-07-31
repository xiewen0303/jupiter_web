package com.jupiter.web;

import com.tron.common.oss.configuration.OSSConfig;
import com.tron.framework.CoreConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@EnableCaching //开启缓存
@MapperScan(basePackages = {"com.jupiter.web.manager.common.dao", "com.jupiter.web.manager.common.base"})
@Import({CoreConfig.class, OSSConfig.class})
public class ConsoleStarter {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(ConsoleStarter.class, args);
    }

}
