package com.lxf.ums.service;


import com.lxf.common.result.JsonResult;

public interface UserService {

    public JsonResult getUser(String name);

    public JsonResult getAll();

    public JsonResult getOrderAndBuy(String orderId) ;

}
