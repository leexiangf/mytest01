package com.lxf.ums;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import springfox.documentation.oas.annotations.EnableOpenApi;

@SpringBootApplication
@MapperScan("com.lxf.ums.mapper")
@EnableAsync
//开启springfox
@EnableOpenApi
//@EnableWebMvc
public class UMSApplication {

    public static void main(String[] args) {
        SpringApplication.run(UMSApplication.class, args);
    }

}
