package com.lxf.mytest01.conf;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.net.UnknownHostException;

/**
 * @Author
 * @Date
 * @Version 1.0
 */
@Configuration   //声明为一个组件
public class MyRedisTemplateConfig {
    //这是一个固定模板，适合大部分应用场景，直接拿去使用即可
    //定义一个自己的redisTemplate,叫MyredisTemplate,不使用默认的
    @Bean
    public RedisTemplate<String, Object> MyredisTemplate(RedisConnectionFactory redisConnectionFactory) throws UnknownHostException {
        //为了方便开发，直接使用<String, Object>泛型
        RedisTemplate<String, Object> template = new RedisTemplate();
        //使用默认的工厂方法
        template.setConnectionFactory(redisConnectionFactory);

        // 序列化配置
        //对json序列化配置
        Jackson2JsonRedisSerializer<Object> objectJackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        //String的序列化
        objectJackson2JsonRedisSerializer.setObjectMapper(om);
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();

        //key采用String序列化的方式
        template.setKeySerializer(stringRedisSerializer);
        //Hash的key采用String序列化的方式
        template.setHashKeySerializer(stringRedisSerializer);
        //value序列化采用jacksond 序列化方式
        template.setValueSerializer(objectJackson2JsonRedisSerializer);
        //hash的value序列化采用jacksond 序列化方式
        template.setHashValueSerializer(objectJackson2JsonRedisSerializer);

        template.afterPropertiesSet();

        return template;
    }
}