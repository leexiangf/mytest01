package com.lxf.redis.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class RedissonConfig {

    @Value("${spring.profiles.active:dev}")
    private String springProfilesActive;

    @Bean
    public RedissonClient redissonClient() throws IOException {
        String redissionFileName = "redission-config-" + springProfilesActive + ".yml";
        System.out.println("redission配置文件名称：" + redissionFileName);
        Config config = Config.fromYAML(RedissonConfig.class.getClassLoader().getResource(redissionFileName));
        return Redisson.create(config);
    }
}
