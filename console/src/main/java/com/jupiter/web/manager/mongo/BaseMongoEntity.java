package com.jupiter.web.manager.mongo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;

/**
 * Created by zhangxiqiang on 2019/7/18.
 */
@Data
public  class BaseMongoEntity implements Serializable {
    @Id
    private String id;
    @Field("add_time")
    private Long addTime;
    @Field("update_time")
    private Long updateTime;
}
