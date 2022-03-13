package com.lxf.mytest01;

import com.alibaba.druid.pool.DruidDataSource;
import com.lxf.mytest01.conf.MyRedisTemplateConfig;
import com.lxf.mytest01.pojo.User;
import com.lxf.mytest01.service.UserService;
import com.lxf.mytest01.utils.RedisUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@SpringBootTest
class Mytest01ApplicationTests {

    @Autowired
    DataSource dataSource;


    @Autowired
    RedisUtil redisUtil;

    @Autowired
    UserService service;



    @Test
    void testStreamSort01() throws SQLException {
    /*    List<String> list1 = Arrays.asList("s", "q", "w", "e");
        List<String> list2 = Arrays.asList("m", "q", "w", "n");
        //交集list2::contains 结果为 q ,w
        List<String> strings1 = list1.stream().filter(list2::contains).collect(Collectors.toList());
        strings1.forEach(s -> System.out.println(s));
        //差集  s , e
        System.out.println("交集-------------------------------------");
        List<String> string2 = list1.stream().filter(s -> !list2.contains(s)).collect(Collectors.toList());
        string2.forEach(s -> System.out.println(s));
        System.out.println("差集-------------------------------------");
        List<String> string3 = list1.parallelStream().collect(Collectors.toList());
        List<String> string4 = list2.parallelStream().collect(Collectors.toList());
        string3.addAll(string4);
        string3.forEach(s -> System.out.println(s));
        System.out.println("合集-------------------------------------");
        List<String> string5 = string3.stream().distinct().collect(Collectors.toList());
        string5.forEach(s -> System.out.println(s));
        System.out.println("去重合集-------------------------------------");*/
        List<User> list1 =new ArrayList<>();
        list1.add(new User(1,"zs",15));
        list1.add(new User(11,"zs",15));
        list1.add(new User(5,"zs",15));
        list1.add(new User(2,"ww",17));
        list1.add(new User(3,"fq",16));
        list1.add(new User(4,"mk",22));
        Map<Integer, User> map = list1.stream().collect(Collectors.toMap(User::getId, User -> User,(k1,k2)->k1));
        map.forEach((integer, user) -> System.out.println("id:"+integer+">>user:"+user));
        List<User> list2 = list1.stream().sorted(Comparator.comparing(User::getAge)
                .thenComparing(Comparator.comparing(User::getId))).collect(Collectors.toList());
        list2.forEach(s -> System.out.println(s));

    }

    @Test
    void testStream02(){
        List<User> list1 =new ArrayList<>();
        list1.add(new User(1,"zs",15));
        list1.add(new User(11,"zs",15));
        list1.add(new User(5,"zs",15));
        list1.add(new User(2,"ww",17));
        list1.add(new User(3,"fq",16));
        list1.add(new User(4,"mk",22));
        //map函数可以改变stream的状态
        // reduce ： 计算对象中某个属性
        Integer ages1 = list1.stream().map(user -> user.getAge()).reduce(0, (a1, a2) -> a1 + a2);
        //另外一种写法
        List<User> users = list1.stream().map(user -> {
            user.setName("");
            return user; //根据Return 返回类型（R）
        }).collect(Collectors.toList());
        //先操作，再求和 ， 例如有打折价格*折扣 再求总价格
        Integer ages2 = list1.stream().map(user -> user.getAge()*user.getId()).reduce(0, (a1, a2) -> a1 + a2);
        users.forEach(System.out::println);
        System.out.println(ages1);
        System.out.println(ages2);
    }

    @Test
    void testRedis01(){
        redisUtil.set("key1","我又拿到了key1");
        System.out.println(redisUtil.get("key1"));//我又拿到了key1

        System.out.println(redisUtil.setIfAbsent("key1", "我重新设置了key1", 10));//false
        System.out.println(redisUtil.get("key1"));//我又拿到了key1
        System.out.println(redisUtil.setIfAbsent("key2", "我设置了key2", 10));
        System.out.println(redisUtil.get("key2"));
        System.out.println(redisUtil.setIfAbsent("key2", "我重新设置了key2", 10));
        System.out.println(redisUtil.get("key2"));

    }

    @Test
    void testRedis02() throws InterruptedException {

 /*       service.getOrderAndBuy("00112233xmx");
        boolean b=true;
        int c = 0;
        while (b){
            if(c==10){
                b=false;
            }
          c++;
            System.out.println(c);
        }*/
        redisUtil.set("k1","v1",5);
        System.out.println(redisUtil.get("k1")+":"+redisUtil.getExpire("k1"));
        int i = 0;
        while (redisUtil.hasKey("k1")){
            if(i>=20){
                break;
            }
            redisUtil.set("k1",i++,2);
            Thread.sleep(3000);
            System.out.println(redisUtil.get("k1")+":"+redisUtil.getExpire("k1"));
        }
        System.out.println("跳出循环");
    }
    @Test
    void testRedis03() throws InterruptedException {
            redisUtil.set("k1","v1",2);
            Thread.sleep(3000);
        System.out.println("是否还有k："+redisUtil.hasKey("k1"));
        System.out.println("过期时间："+redisUtil.getExpire("k1"));
        redisUtil.expire("k1",3);
        System.out.println("---是否还有k："+redisUtil.hasKey("k1"));
        System.out.println("---过期时间："+redisUtil.getExpire("k1"));
        System.out.println("---获取值："+redisUtil.get("k1"));
    }
    @Test
    void testAtomic01() throws InterruptedException {
        AtomicInteger atomicInteger = new AtomicInteger(11);
        atomicInteger.incrementAndGet();
        System.out.println(atomicInteger.get());
        atomicInteger.decrementAndGet();
        System.out.println(atomicInteger.get());
        System.out.println(atomicInteger.addAndGet(50));
    }
}
