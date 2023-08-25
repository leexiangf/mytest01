package com.lxf.eas.service;

import com.lxf.common.function.SupplierThrow;
import com.promotion.redis.util.RedissonUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * @Classname LockService
 * @Description   redisson上锁执行function
 * @Date 2023/7/24 15:02
 * @Author lxf
 */
@Component
@Slf4j
public class LockService {

    @Autowired
    private RedissonUtil redissonUtil;


    public <T,X extends Throwable> T executeTryLock(String key, long tryTime, long lockTime, TimeUnit timeUnit,
                                                    SupplierThrow<T> supplier, Supplier<X> exceptionSupplier)  throws Throwable{
        RLock lock = redissonUtil.getLock( key);
        if(lock.isLocked()){
            throw  exceptionSupplier.get();
        }
        try {
            if(lock.tryLock(tryTime, lockTime, timeUnit)){
                log.warn("execute lock: {}",key);
                return supplier.get();
            }else {
                throw  exceptionSupplier.get();
            }
        }finally {
            if(lock.isLocked() && lock.isHeldByCurrentThread()){
                log.warn("execute unlock: {}",key);
                lock.unlock();
            }
        }
    }

    public <T,X extends Throwable> T executeLock(String key, long lockTime, TimeUnit timeUnit,
                                 SupplierThrow<T> supplier,Supplier<X> exceptionSupplier) throws Throwable {
        RLock lock = redissonUtil.getLock( key);
        if(lock.isLocked()){
            throw  exceptionSupplier.get();
        }
        try {
            log.warn("execute lock: {}",key);
            lock.lock(lockTime,timeUnit);
            return supplier.get();
        }finally {
            if(lock.isLocked() && lock.isHeldByCurrentThread()){
                log.warn("execute unlock: {}",key);
                lock.unlock();
            }
        }
    }



}
