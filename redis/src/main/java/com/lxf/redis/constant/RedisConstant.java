package com.lxf.redis.constant;


import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author cb
 * @blame cb
 * @date 2020/7/27 14:56
 */
public class RedisConstant {

    /**
     * redis命名空间
     **/
    public static final String NAME_SPACE = "xxx:yyy:";

    /**业务处理中**/
    private static final String BUSINESS_DEALING = NAME_SPACE + "dealing:%s";

    /**
     * 缓存机制超时时间
     **/
    public static final int CACHE_EXPIRE = 10;
    /**
     * 缓存机制存储短时间
     **/
    public static final String CACHE_FUN_NAME = "ttlRedisCacheManager";
    /**
     * 公共缓存键方法
     **/
    public static final String CACHE_KEY_GENERATE = "myCommonKey";
    /**
     * 缓存机制存储 空间
     **/
    public static final String CACHE_NAME_SPACE = NAME_SPACE + "cache:";


    /**
     * 限流键
     * @param key
     * @return
     */
    public static String rateLimit(String key){
        return  NAME_SPACE + "ratelimit:" + key;
    }

    /**
     * 并发锁
     * @param keys
     * @return
     */
    public static String lockKey(Object... keys){
        return NAME_SPACE + "lock:" + Stream.of(keys).map(String::valueOf).collect(Collectors.joining(":"));
    }

    /**
     * 登录校验码
     */
    public static final String LOGIN_KEY = NAME_SPACE + "img:key:";

    /**
     * 自增标识
     * @param keys
     * @return
     */
    public static String increKey(String... keys){
        return NAME_SPACE + "key:incre:" + String.join(":",keys);
    }



    public static String getLoginKey(String userId,int platformTypeCode){
        return NAME_SPACE + "login:" + userId + ":" + platformTypeCode;
    }


    /**
     * 业务处理中的键
     * @param orderId
     * @return
     */
    public static String getDealingKey(String orderId){
        return String.format(BUSINESS_DEALING,orderId);
    }


    /**
     * 处理中业务 缓存的失效时间 秒
     **/
    public static final long DEALING_EXPIRE = 10;

    /**
     * 创建的表
     * @param tableName
     * @return
     */
    public static String tableKey(String tableName){
        return NAME_SPACE + "key:table:" + tableName;
    }
}
