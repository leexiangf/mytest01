package com.lxf.eas.aspect;

import com.lxf.common.annotation.RedissonLimit;
import com.lxf.eas.utils.ProceedingJoinPointUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RateIntervalUnit;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @Classname RedisLimitAspect
 * @Description redis限流
 * @Date 2023/7/27 15:21
 * @Author lxf
 */
@Aspect
@Component
@Slf4j
public class RedisLimitAspect {

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private ProceedingJoinPointUtil proceedingJoinPointUtil;


    @Around(value = "@annotation(com.promotion.common.annotation.RedissonLimit)")
    public Object around(ProceedingJoinPoint joinPoint) throws Exception {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        RedissonLimit redissonLimit = method.getAnnotation(RedissonLimit.class);
        Class<?> objClass = method.getReturnType();
        Object object = objClass.newInstance();
        Object result = new Object();
        try {
            Object generateKey = proceedingJoinPointUtil.getKey(joinPoint, redissonLimit);
            //redis key
            String key = redissonLimit.prefix() + generateKey.toString();
            log.warn("rateLimiter key :{}",key);
            //声明一个限流器
            RRateLimiter rateLimiter = redissonClient.getRateLimiter(key);
            //设置速率，time秒中产生count个令牌
            rateLimiter.trySetRate(RateType.OVERALL, redissonLimit.count(), redissonLimit.time(), RateIntervalUnit.SECONDS);
            // 试图获取一个令牌，获取到返回true
            boolean tryAcquire = rateLimiter.tryAcquire();
            if (!tryAcquire) {
                throw new RuntimeException("操作频繁，请稍后再试!");
            }
            result = joinPoint.proceed();
        } catch (Throwable throwable) {
            log.info("请求参数：{}", Arrays.toString(joinPoint.getArgs()));
            throw new RuntimeException(throwable.getMessage());
        }
        return result;
    }
}
