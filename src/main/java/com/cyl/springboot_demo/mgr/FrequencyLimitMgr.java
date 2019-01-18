package com.cyl.springboot_demo.mgr;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * redis限流
 */
@Component
public class FrequencyLimitMgr {
    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    InetAddress inetAddress;

    public static RedisScript<Boolean> redisScript;

    static {
        redisScript = new DefaultRedisScript<>();
        ((DefaultRedisScript<Boolean>) redisScript).setResultType(Boolean.class);
        ((DefaultRedisScript<Boolean>) redisScript).setScriptSource(new ResourceScriptSource(new ClassPathResource("lua/frequencyLimit.lua")));
    }

    public Boolean isLimit(String key, Long time, Integer max){
        String realKey = key +"_"+inetAddress.getHostAddress();
        List<String> list = new ArrayList();
        list.add(realKey);
        Boolean limit = stringRedisTemplate.execute(redisScript,list, String.valueOf(time), String.valueOf(max));
        return limit;
    }
}
