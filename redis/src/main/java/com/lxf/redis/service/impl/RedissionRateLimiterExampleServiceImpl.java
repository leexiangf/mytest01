package com.lxf.redis.service.impl;

import com.lxf.redis.service.RedissionRateLimiterExampleService;
import com.promotion.redis.constant.RedisConstant;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RateIntervalUnit;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @Author chb
 * @Date 2023/6/29 16:11
 */
@Component
public class RedissionRateLimiterExampleServiceImpl implements RedissionRateLimiterExampleService {


    @Autowired
    private RedissonClient redissonClient;

    private RRateLimiter rRateLimiter;

    @PostConstruct
    public void initRateLimiter(){
        RRateLimiter ra = redissonClient.getRateLimiter(RedisConstant.rateLimit("test"));
        ra.setRate(RateType.OVERALL,6,1, RateIntervalUnit.MINUTES);
        rRateLimiter = ra;
    }

    @Override
    public boolean testRateLimiter() {
        return rRateLimiter.tryAcquire();
    }
}
