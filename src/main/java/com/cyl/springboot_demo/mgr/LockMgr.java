package com.cyl.springboot_demo.mgr;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName MoldLockMgr
 * @Author dianXiao2
 * @Date 2019/1/5 17:26
 * redis分布式锁
 **/
@Service
public class LockMgr {


    @Autowired
    StringRedisTemplate stringRedisTemplate;

    private static final String DEFAULT_VALUE = "coupon_default";

    public static RedisScript<Boolean> redisScript;

    static {
        redisScript = new DefaultRedisScript<>();
        ((DefaultRedisScript<Boolean>) redisScript).setResultType(Boolean.class);
        ((DefaultRedisScript<Boolean>) redisScript).setScriptSource(new ResourceScriptSource(new ClassPathResource("lua/lock.lua")));
    }

    /**
     * 加锁
     * @param key
     * @param expireTime
     * @param timeout
     * @return
     */
    public Boolean  lock(String key, Long expireTime , Long timeout)  {

        if (expireTime == null){
            expireTime = 3000L;
        }

        if(timeout == null){
            timeout = 2000L;
        }

        TimeUnit.MILLISECONDS.toNanos(TimeUnit.MILLISECONDS.toNanos(timeout));

        List<String> keys = new ArrayList<>();
        keys.add(key);
        Boolean lock = Boolean.FALSE;
        long start = System.nanoTime();
        long end = start;
        while ((end - start) < timeout){
            lock = stringRedisTemplate.execute(redisScript,keys, DEFAULT_VALUE, String.valueOf(expireTime));
            if(lock){
                return lock;
            }else{
                try {
                    TimeUnit.MILLISECONDS.sleep(100L + new Random().nextInt(100));
                    end = System.nanoTime();
                } catch (InterruptedException e) {
                    end = System.nanoTime();
                }
            }
        }

        return lock;
    }

    public void releaseLock(String key){
        stringRedisTemplate.delete(key);
    }

}
