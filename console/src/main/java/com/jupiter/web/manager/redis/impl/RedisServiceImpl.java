package com.jupiter.web.manager.redis.impl;

import com.alibaba.fastjson.JSONArray;
import com.jupiter.web.manager.redis.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class RedisServiceImpl implements RedisService {

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @Override
    public Set<String> keys(String pattern) {
        return redisTemplate.keys(pattern);
    }

    @Override
    public Set<Object> smembers(String key) {
        try {
            return redisTemplate.opsForSet().members(key);
        } catch (Exception e) {
            log.error("redis error: ", e);
            return null;
        }
    }

    /**
     * Check if set at {@code key} contains {@code value}.
     *
     * @param key   键
     * @param value
     * @return true 存在 false不存在
     */
    @Override
    public boolean sHasKey(String key, Object value) {
        try {
            return redisTemplate.opsForSet().isMember(key, value);
        } catch (Exception e) {
            log.error("redis error: ", e);
            return false;
        }
    }

    /**
     * 将数据放入set缓存
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 成功个数
     */
    @Override
    public long sAdd(String key, Object... values) {
        try {
            return redisTemplate.opsForSet().add(key, values);
        } catch (Exception e) {
            log.error("redis error: ", e);
            return 0;
        }
    }

    /**
     * 将set数据放入缓存
     *
     * @param key    键
     * @param time   时间(秒)
     * @param values 值 可以是多个
     * @return 成功个数
     */
    @Override
    public long sSetAndTime(String key, long time, Object... values) {
        if (time <= 0) {
            throw new RuntimeException("过期时间必须大于等于0");
        }
        try {
            Long count = redisTemplate.opsForSet().add(key, values);
            expire(key, time);
            return count;
        } catch (Exception e) {
            log.error("redis error: ", e);
            return 0;
        }
    }

    /**
     * 获取set缓存的长度
     *
     * @param key 键
     * @return
     */
    @Override
    public long sGetSize(String key) {
        return redisTemplate.opsForSet().size(key);
    }

    /**
     * 移除值为value的
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 移除的个数
     */
    @Override
    public long sRemove(String key, Object... values) {
        Long count = redisTemplate.opsForSet().remove(key, values);
        return count;
    }

    /**
     * 获取list缓存的内容
     *
     * @param key   键
     * @param start 开始
     * @param end   结束  0 到 -1代表所有值
     * @return
     */
    @Override
    public List<Object> listRange(String key, long start, long end) {
        try {
            return redisTemplate.opsForList().range(key, start, end);
        } catch (Exception e) {
            log.error("redis error: ", e);
            return null;
        }
    }

    /**
     * 获取list缓存的长度
     *
     * @param key 键
     * @return
     */
    @Override
    public long listSize(String key) {
        try {
            return redisTemplate.opsForList().size(key);
        } catch (Exception e) {
            log.error("redis error: ", e);
            return 0;
        }
    }

    /**
     * 通过索引 获取list中的值
     *
     * @param key   键
     * @param index 索引  index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
     * @return
     */
    @Override
    public Object listGet(String key, long index) {
        try {
            return redisTemplate.opsForList().index(key, index);
        } catch (Exception e) {
            log.error("redis error: ", e);
            return null;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @return
     */
    @Override
    public boolean listRightPush(String key, Object value) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            return true;
        } catch (Exception e) {
            log.error("redis error: ", e);
            return false;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return
     */
    @Override
    public boolean listRightPush(String key, Object value, long time) {
        if (time <= 0) {
            throw new RuntimeException("过期时间必须大于等于0");
        }
        try {
            redisTemplate.opsForList().rightPush(key, value);
            expire(key, time);
            return true;
        } catch (Exception e) {
            log.error("redis error: ", e);
            return false;
        }
    }

    @Override
    public <T> T listLeftPop(String key) {
        return key == null ? null : (T) redisTemplate.opsForList().leftPop(key);
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @return
     */
    @Override
    public boolean listPushAll(String key, List<Object> value) {
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            return true;
        } catch (Exception e) {
            log.error("redis error: ", e);
            return false;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return
     */
    @Override
    public boolean lSet(String key, List<Object> value, long time) {
        if (time <= 0) {
            throw new RuntimeException("过期时间必须大于等于0");
        }
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            expire(key, time);
            return true;
        } catch (Exception e) {
            log.error("redis error: ", e);
            return false;
        }
    }

    /**
     * 根据索引修改list中的某条数据
     *
     * @param key   键
     * @param index 索引
     * @param value 值
     * @return
     */
    @Override
    public boolean listSetIndex(String key, long index, Object value) {
        try {
            redisTemplate.opsForList().set(key, index, value);
            return true;
        } catch (Exception e) {
            log.error("redis error: ", e);
            return false;
        }
    }

    /**
     * 移除N个值为value
     * count 的值可以是以下几种：
     * count > 0 : 从表头开始向表尾搜索，移除与 value 相等的元素，数量为 count 。
     * count < 0 : 从表尾开始向表头搜索，移除与 value 相等的元素，数量为 count 的绝对值。
     * count = 0 : 移除表中所有与 value 相等的值。
     *
     * @param key   键
     * @param count 移除多少个
     * @param value 值
     * @return 移除的个数
     */
    @Override
    public long listRemove(String key, long count, Object value) {
        try {
            Long remove = redisTemplate.opsForList().remove(key, count, value);
            return remove;
        } catch (Exception e) {
            log.error("redis error: ", e);
            return 0;
        }
    }


    @Override
    public void publish(final String key, final String value) {
        redisTemplate.convertAndSend(key, value);
    }

    /**
     * 递增
     *
     * @param key 键
     * @return
     */
    @Override
    public Long incr(final String key) {
        return redisTemplate.opsForValue().increment(key);
    }

    /**
     * 递增
     *
     * @param key   键
     * @param delta 要增加几(大于0)
     * @return
     */
    @Override
    public Long incrby(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递增因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * 递减
     *
     * @param key 键
     * @return
     */
    @Override
    public long decr(String key) {
        return redisTemplate.opsForValue().decrement(key);
    }

    /**
     * 递减
     *
     * @param key   键
     * @param delta 要减少几(小于0)
     * @return
     */
    @Override
    public long decrby(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递减因子必须大于0");
        }
        return redisTemplate.opsForValue().decrement(key, delta);
    }

    /**
     * 指定缓存失效时间
     *
     * @param key     键
     * @param seconds 时间(秒)
     * @return
     */
    @Override
    public boolean expire(final String key, final long seconds) {
        if (seconds <= 0) {
            throw new RuntimeException("过期时间必须大于0");
        }
        try {
            redisTemplate.expire(key, seconds, TimeUnit.SECONDS);
            return true;
        } catch (Exception e) {
            log.error("redis error: ", e);
            return false;
        }
    }

    /**
     * 根据key 获取过期时间
     *
     * @param key 键 不能为null
     * @return 时间(秒) 返回0代表为永久有效
     */
    @Override
    public long getExpire(final String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 判断key是否存在
     *
     * @param key 键
     * @return true 存在 false不存在
     */
    @Override
    public boolean hasKey(String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            log.error("redis error: ", e);
            return false;
        }
    }

    /**
     * 删除缓存
     *
     * @param key 可以传一个值 或多个
     */
    @Override
    public void delete(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                redisTemplate.delete(key[0]);
            } else {
                redisTemplate.delete(CollectionUtils.arrayToList(key));
            }
        }
    }

    @Override
    public void delete(Collection<String> keys) {
        if (keys != null && keys.size() > 0) {
            redisTemplate.delete(keys);
        }
    }

    /**
     * 普通缓存获取
     *
     * @param key 键
     * @return 值
     */
    @Override
    public <T> T get(String key) {
        return key == null ? null : (T) redisTemplate.opsForValue().get(key);
    }

    @Override
    public <T> List<T> getListBean(String key, Class<T> clazz) {
        String value = get(key);
        JSONArray arr = JSONArray.parseArray(value);
        return arr.toJavaList(clazz);
    }

    /**
     * 普通缓存放入  没有过期时间
     *
     * @param key   键
     * @param value 值
     * @return true成功 false失败
     */
    @Override
    public boolean set(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            log.error("redis error: ", e);
            return false;
        }
    }

    @Override
    public boolean setFixTime(String key, Object value) {
        return set(key, value, 7200);//默认2个小时过期
    }

    /**
     * 普通缓存放入并设置时间
     *
     * @param key     键
     * @param value   值
     * @param seconds 时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    @Override
    public boolean set(String key, Object value, long seconds) {
        try {
            if (seconds > 0) {
                redisTemplate.opsForValue().set(key, value, seconds, TimeUnit.SECONDS);
            } else {
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            log.error("redis error: ", e);
            return false;
        }
    }

    @Override
    public boolean setNX(final String key, final Object value, long seconds) {
        return redisTemplate.opsForValue().setIfAbsent(key, value, seconds, TimeUnit.SECONDS);
    }

}
