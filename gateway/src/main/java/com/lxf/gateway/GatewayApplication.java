package com.lxf.gateway;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @ClassName : GatewayApplication
 * @Description
 * @Date 2022/4/17 16:26
 * @Created lxf
 */
@EnableFeignClients(basePackages = {"com.lxf.gateway.feign"})
@SpringBootApplication
@EnableConfigurationProperties
@MapperScan({"com.lxf.gateway.dao.mapper"})
@ComponentScan(basePackages = {"com.lxf.common", "com.lxf.redis", "com.lxf.gateway"})
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }
}
