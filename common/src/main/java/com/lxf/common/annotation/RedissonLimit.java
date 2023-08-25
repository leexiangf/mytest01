package com.lxf.common.annotation;

import com.lxf.common.enums.LimitType;

import java.lang.annotation.*;

/**
 * @Classname RedissonLimit
 * @Description redisson限流
 * @Date 2023/7/27 15:14
 * @Author lxf
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface RedissonLimit {

    String prefix() default "rateLimit:";

    /**
     * LimitType.CUSTOM 自定义时的限流自定义key
     */
    String key() default "CUSTOM";

    /**
     * 限流单位时间（单位为s）
     */
    int time() default 5;

    /**
     * 单位时间内限制的访问次数
     */
    int count() default 10;

    /**
     * 限流类型
     */
    LimitType type() default LimitType.CUSTOM;


}
