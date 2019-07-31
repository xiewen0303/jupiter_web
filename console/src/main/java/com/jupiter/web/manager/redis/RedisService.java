package com.jupiter.web.manager.redis;

import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Repository
public interface RedisService {
    Set<String> keys(String pattern);

    Set<Object> smembers(String key);

    boolean sHasKey(String key, Object value);

    long sAdd(String key, Object... values);

    long sSetAndTime(String key, long time, Object... values);

    long sGetSize(String key);

    long sRemove(String key, Object... values);

    List<Object> listRange(String key, long start, long end);

    long listSize(String key);

    Object listGet(String key, long index);

    boolean listRightPush(String key, Object value);

    boolean listRightPush(String key, Object value, long seconds);

    <T> T listLeftPop(String key);

    boolean listPushAll(String key, List<Object> value);

    boolean lSet(String key, List<Object> value, long time);

    boolean listSetIndex(String key, long index, Object value);

    long listRemove(String key, long count, Object value);

    void publish(final String key, final String value);

    Long incr(final String key);

    Long incrby(String key, long delta);

    long decr(String key);

    long decrby(String key, long delta);

    boolean expire(final String key, final long seconds);

    long getExpire(final String key);

    boolean hasKey(String key);

    void delete(String... key);

    void delete(Collection<String> keys);

    <T> T get(String key);

    <T> List<T> getListBean(String key, Class<T> clazz);

    boolean setFixTime(String key, Object value);

    boolean set(String key, Object value);

    boolean set(String key, Object value, long seconds);

    boolean setNX(final String key, final Object value, long seconds);

}
