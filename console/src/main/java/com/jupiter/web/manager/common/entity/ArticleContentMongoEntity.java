package com.jupiter.web.manager.common.entity;

import com.jupiter.web.manager.mongo.BaseMongoEntity;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by zhangxiqiang on 2019/7/18.
 */
@Document(collection = "article_info")
@Data
public class ArticleContentMongoEntity extends BaseMongoEntity{
    private String data;
}
