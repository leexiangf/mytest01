package com.lxf.eas;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import org.springframework.scheduling.annotation.EnableAsync;
import springfox.documentation.oas.annotations.EnableOpenApi;

@SpringBootApplication
@MapperScan("com.lxf.eas.mapper")
@EnableAsync
//开启springfox
@EnableOpenApi
//@EnableWebMvc
@EnableAutoConfiguration(exclude={DruidDataSourceAutoConfigure.class})
public class EASApplication {

    public static void main(String[] args) {
        SpringApplication.run(EASApplication.class, args);
    }

}
