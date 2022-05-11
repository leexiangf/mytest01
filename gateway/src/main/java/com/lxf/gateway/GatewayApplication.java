package com.lxf.gateway;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * @ClassName : GatewayApplication
 * @Description
 * @Date 2022/4/17 16:26
 * @Created lxf
 */
@SpringBootApplication
@EnableDiscoveryClient
//@RefreshScope
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }
}
