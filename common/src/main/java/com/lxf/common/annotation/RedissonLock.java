package com.lxf.common.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * @Classname RedissonLock
 * @Description redisson lock Annotation
 * @Date 2023/7/24 14:33
 * @Author lxf
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RedissonLock {

    @AliasFor("key")
    String value() default "";
    /** redis key */
    String key() default "";
    /** key前缀 */
    String preKey() default "AUTO_PREFIX:";
    /** 尝试获取锁时间 -1为不等待直接失败*/
    long tryTime() default -1L;
    /** 加锁时间 */
    long lockTime() default 5000L;
    /** 时间单位 */
    TimeUnit timeUnit() default TimeUnit.MILLISECONDS;
    /** 报错编码*/
    String code() default "400";
    /** 报错信息*/
    String msg() default "param error";

}
