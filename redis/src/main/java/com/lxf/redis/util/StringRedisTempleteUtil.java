package com.lxf.redis.util;

import com.lxf.redis.constant.RedisConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;


@Component
public class StringRedisTempleteUtil {
 
    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    public String getIncrementNum(String type, String date, int baseNum, Duration expire){
        String key = RedisConstant.NAME_SPACE + date + ":" + type;
        Long increment = stringRedisTemplate.opsForValue().increment(key, 1);
        stringRedisTemplate.expire(key,expire);
        return String.valueOf(baseNum + increment);
    }

}