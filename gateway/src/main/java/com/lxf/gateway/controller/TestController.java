package com.lxf.gateway.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName : TestController
 * @Description
 * @Date 2022/4/18 23:26
 * @Created lxf
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @Value("${myser.it}")
    private String myser;

    @GetMapping("/myser")
    public String test(){
        return myser;
    }
}
