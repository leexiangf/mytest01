package com.lxf.mytest01.controller;

import com.lxf.mytest01.pojo.User;
import com.lxf.mytest01.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {
    @Autowired
    private UserServiceImpl service;

    @GetMapping("/byname")
    public User getUserByName(@RequestParam("name") String name){
        return service.getUser(name);
    }

    @GetMapping("/getAll")
    public List<User> getAll(){
        return service.getAll();
    }

    @GetMapping("/getOrderAndBuy")
    public String getOrderAndBuy(@RequestParam("goodsId")String goodsId)  {
        return service.getOrderAndBuy(goodsId);
    }
}
