package com.jupiter.web.manager.mongo;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import org.springframework.util.ReflectionUtils;

import javax.annotation.Resource;
import java.util.Objects;

@Repository
@Slf4j
public class MongoDao {
    @Setter
    @Resource
    private MongoTemplate mongoTemplate;
    private static final String MONGO_ID_KEY = "_id";

    /**
     * 保存
     *
     * @param t
     * @param <T>
     * @return
     */
    public <T extends BaseMongoEntity> T add(T t) {
        Objects.requireNonNull(t, "mongo添加对象不能为空");
        checkDocumentAnnotation(t);
        Long now = System.currentTimeMillis();
        t.setAddTime(now);
        t.setUpdateTime(now);
        return mongoTemplate.save(t);
    }

    /**
     * 校验对象是否存在@Document注解，如果不存在抛出异常，存在就返回document名称
     *
     * @param t
     * @param <T>
     * @return
     */
    private <T> String checkDocumentAnnotation(T t) {
        Objects.requireNonNull(t, "校验对象不能为空");
        Document document = t.getClass().getAnnotation(Document.class);
        if (document == null) {
            throw new RuntimeException(String.format("对象:%s不存在注解@org.springframework.data.mongodb.core.mapping.Document", t.getClass()));
        }
        String value = null;
        if (StringUtils.isNotBlank(document.value())) {
            value = document.value();
        } else {
            value = document.collection();
        }
        Objects.requireNonNull(value, "mongo的集合名称不能为空");
        return value;
    }


    public <T extends BaseMongoEntity> T findById(String id, Class<T> clazz) {
        Objects.requireNonNull(id, "id不能为空");
        Objects.requireNonNull(clazz, "mongo实体对象不能为空");
        return mongoTemplate.findById(id, clazz);
    }

    public <T extends BaseMongoEntity> UpdateResult updateById(T t) {
        Query query = new Query();
        query.addCriteria(new Criteria(MONGO_ID_KEY).is(t.getId()));
        return update(t, query);
    }

    /**
     * 更新数据
     *
     * @param t
     * @param query
     * @param <T>
     * @return
     */
    public <T extends BaseMongoEntity> UpdateResult update(T t, Query query) {
        Objects.requireNonNull(t, "mongo更新对象不能为空");
        Objects.requireNonNull(t.getId(), "id不能为空");
        String documentName = checkDocumentAnnotation(t);

        Update update = new Update();
        ReflectionUtils.doWithFields(t.getClass(), field -> {
            field.setAccessible(true);
            if (field.get(t) != null) {
                Id annotationId = field.getAnnotation(Id.class);
                if (annotationId == null) {
                    // @Id不能存在
                    Field mongoField = field.getAnnotation(Field.class);
                    if (mongoField != null) {
                        update.set(mongoField.value(), field.get(t));
                    } else {
                        update.set(field.getName(), field.get(t));
                    }
                }
            }
        });
        // update_time单独更新
        update.set("update_time", System.currentTimeMillis());
        return mongoTemplate.upsert(query, update, documentName);

    }

    /**
     * @param <T>
     * @return
     */
    public  <T extends BaseMongoEntity> DeleteResult removeById(T t) throws Exception {
        Objects.requireNonNull(t, "mongo更新对象不能为空");
        Objects.requireNonNull(t.getId(), "id不能为空");
        return mongoTemplate.remove(t);
    }

}
