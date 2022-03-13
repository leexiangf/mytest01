package com.lxf.mytest01;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@MapperScan("com.lxf.mytest01.mapper")
@EnableAsync
public class Mytest01Application {

    public static void main(String[] args) {
        SpringApplication.run(Mytest01Application.class, args);
    }

}
