package com.lxf.mytest01.async;


import com.lxf.mytest01.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;


@Component
@Async
@Slf4j
public class MyAsync {


    @Autowired
    RedisUtil redisUtil;
    //@Async
    public void asyncThread(String goodsId, UUID uuid){

        log.info("进入异步方法");
        log.info("异步线程名："+Thread.currentThread().getName());
        //boolean hasKey = redisUtil.hasKey(goodsId);
        //long expire = redisUtil.getExpire(goodsId);
        while (true){
            //传入的v值不等于当前用户操作的v值，则停止(防止在用户删除这个key后进入此方法的同时，有另外用户创建了这个key)
            if(!uuid.toString().equals(redisUtil.get(goodsId))||
                    !redisUtil.hasKey(goodsId)||
                    redisUtil.getExpire(goodsId)<=0)
                break;
           /*
           TODO 另外一个问题：当执行到这一步时当前用户正好删除了key，另外用户添加了key，会产生哪些问题
           TODO  两个异步线程用时操作一个key会产生哪些问题，应该可以参考一下redis线程模型
           */
            if (redisUtil.hasKey(goodsId)&&redisUtil.getExpire(goodsId)<=3&&redisUtil.getExpire(goodsId)>=0){
                redisUtil.expire(goodsId,10);
                log.info("进入延时key的线程未重置的过期时间："+redisUtil.getExpire(goodsId));
            }
            log.info("进入延时key的线程重置的过期时间："+redisUtil.getExpire(goodsId));
        }
        log.info("结束异步循环");
    }
}
