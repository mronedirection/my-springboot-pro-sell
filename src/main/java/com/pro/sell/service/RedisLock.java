package com.pro.sell.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * Redis实现分布式锁：
 * 使用Redis对一段代码进行加锁，解决高并发情况下超卖的问题；
 * 特点：
 * 支持分布式；
 * 可以更细粒度的控制；
 * 多台机器上多个进程对一个数据进行操作的互斥；
 */
@Component
@Slf4j
public class RedisLock {

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 加锁
     * @param key
     * @param value 当前时间+超时时间
     * @return
     */
    //死锁：如果第一个线程拿到锁，由于执行出错，导致未能释放锁，则其它线程由于锁未被释放所以一直拿不到锁，这种现象称为死锁；
    public boolean lock(String key, String value) {
        //如果当前线程拿到锁，则直接返回true
        //如果key不存在则保存后返回true，说明获得锁；
        if(redisTemplate.opsForValue().setIfAbsent(key, value)) {
            return true;
        }
        //如果当前线程没有拿到锁，判断拿到锁的线程有没有过期
        //currentValue=A   这两个线程的value都是B  其中一个线程拿到锁
        String currentValue = redisTemplate.opsForValue().get(key);
        //如果锁过期
        if (!StringUtils.isEmpty(currentValue)
                && Long.parseLong(currentValue) < System.currentTimeMillis()) {
            //如果第一个线程拿到锁后，由于出现异常并未释放锁，并且锁已过期，则其它线程依旧可以拿到锁，不会造成死锁现象
            //获取上一个锁的时间
            String oldValue = redisTemplate.opsForValue().getAndSet(key, value);
            if (!StringUtils.isEmpty(oldValue) && oldValue.equals(currentValue)) {
                return true; //第二个线程拿到新锁
            }
        }
        //如果锁未过期，则其它线程无法拿到锁，无法修改库存
        return false;
    }

    /**
     * 解锁
     * @param key
     * @param value
     */
    public void unlock(String key, String value) {
        try {
            String currentValue = redisTemplate.opsForValue().get(key);
            if (!StringUtils.isEmpty(currentValue) && currentValue.equals(value)) {
                redisTemplate.opsForValue().getOperations().delete(key);
            }
        }catch (Exception e) {
            log.error("【redis分布式锁】解锁异常, {}", e);
        }
    }

}
