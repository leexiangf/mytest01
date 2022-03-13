package com.lxf.mytest01.service;

import com.lxf.mytest01.pojo.User;
import org.springframework.stereotype.Service;

import java.util.List;


public interface UserService {

    public User getUser(String name);

    public List<User> getAll();

    public String getOrderAndBuy(String orderId) ;

}
