package com.lxf.ums.lock.redisson.serveice;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @Classname RedissonService
 * @Description
 * @Date 2023/3/31 16:25
 * @Author lxf
 */
@Component
@Slf4j
public class RedissonService {

    @Autowired
    private RedissonClient redissonClient;

    private final String preKey = "JOIN_";

    public String join(String userId) {
        log.info("user in:{}",userId);
        RLock rLock = redissonClient.getLock(preKey + userId);
        try {
            boolean tryLock = rLock.tryLock(1, 10, TimeUnit.SECONDS);
            System.out.println(tryLock);
            System.out.println(userId);
        }catch (InterruptedException e){
            e.printStackTrace();
        }finally {
            rLock.unlock();
        }
        return userId;
    }
}
