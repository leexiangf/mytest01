package com.lxf.netty_server;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import springfox.documentation.oas.annotations.EnableOpenApi;

@SpringBootApplication(scanBasePackages = {"com.lxf"})
@MapperScan("com.lxf.netty_server.mapper")
@EnableAsync
//开启springfox
@EnableOpenApi
//@EnableWebMvc
public class NettyServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(NettyServerApplication.class, args);
    }

}
