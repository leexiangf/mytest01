package com.lxf.ums.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.lxf.common.result.JsonResult;
import com.lxf.redis.constant.RedisConstant;
import com.lxf.ums.async.MyAsync;
import com.lxf.ums.mapper.UserMapper;
import com.lxf.ums.pojo.entity.User;
import com.lxf.ums.service.UserService;
import com.lxf.ums.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    volatile MyAsync myAsync;

    @Override
    public JsonResult getUser(String name) {
        return JsonResult.OK();
    }

    @Override
    public JsonResult getAll() {
       // List<User> userList = userMapper.getAll();
        List<User> userList= userMapper.selectList(Wrappers.emptyWrapper());
        return JsonResult.success(userList);
    }


    @Cacheable(cacheManager = RedisConstant.CACHE_FUN_NAME,value = "user",key = "#name")
    public User getByName(String name){
        return new User();
    }


    /**
     * 使用redis实现分布式锁
     *    例如多个服务器集群下抢购一件商品，使商品库存数安全执行
     * @param goodsId 商品id
     * @return
     */
    @Override
    public JsonResult getOrderAndBuy(String goodsId)  {
        log.info("当前线程："+Thread.currentThread().getName());
        //1.根据goodsId查询到商品数量，这里就直接定义一个数
         AtomicInteger count = new AtomicInteger(50);
        //2.执行商品数量减少,但是在分布式下同步代码块并不能解决多个服务器的共享数据一致性
        //所以需要使用其他工具保证,例如redis,zk
        UUID uuid = UUID.randomUUID();
        try {
            //2.1 把商品id设置为key，使用一个随机数有效防止其他当前商品的操作释放了这个锁
            //redisUtil.set(goodsId,uuid,10);
            //3. 另起一个线程来维持这个key的过期时间
/*            new Thread(new Runnable() {
                @Override
                public void run() {
                    log.info("进入 异步线程："+Thread.currentThread().getName()+":"+redisUtil.getExpire(goodsId));
                    while (redisUtil.hasKey(goodsId)&&redisUtil.getExpire(goodsId)<5l){
                        redisUtil.expire(goodsId,10);
                        log.info("进入延时key的线程重置过期时间："+redisUtil.getExpire(goodsId));
                    }
                    log.info("离开 异步线程："+Thread.currentThread().getName()+":"+redisUtil.getExpire(goodsId));
                }
            }).start();*/
            //myAsync.asyncThread(goodsId);
            //2.2 使用redis.setnx方法来实现锁（唯一进入）
            while (true) {
                if(count.get()<=0)
                    break;
                if(redisUtil.setIfAbsent(goodsId,uuid.toString(),10)){
                    myAsync.asyncThread(goodsId,uuid);
                    //模拟超时操作
                    log.info("1111--------key的过期时间："+redisUtil.getExpire(goodsId));
                    //这里来实现具体的业务操作
                    //Thread.sleep(7000);
                    log.info("2222--------key的过期时间："+redisUtil.getExpire(goodsId));
                    count.getAndDecrement();
                    log.info("商品数量剩余："+count);
                    break;
                }
            }
            //以上代码使用redission可以实现原子性
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            //4.最终通过对比goodsId里面的值，来确定是当前执行的，然后删除key（以防其他线程把这个key删除了）
            //注：uuid.equals goodsid 把我坑了 ，必须要tostring才行
            if(uuid.toString().equals(redisUtil.get(goodsId))) redisUtil.del(goodsId);
            log.info("删除后的key："+redisUtil.get(goodsId));
        }

        return JsonResult.success(count);
    }

}
