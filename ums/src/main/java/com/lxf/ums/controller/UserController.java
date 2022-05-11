package com.lxf.ums.controller;


import com.lxf.common.result.JsonResult;
import com.lxf.ums.pojo.request.SmsLoginRequest;
import com.lxf.ums.pojo.request.UserLoginRequest;
import com.lxf.ums.security.service.LoginServiceImp;
import com.lxf.ums.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = {"用户信息相关"})
@RestController
@Slf4j
public class UserController {

    @Autowired
    private UserService service;

    @Autowired
    private LoginServiceImp loginServiceImp;

    @ApiOperation(value = "user登录")
    @PostMapping("/login")
    public JsonResult login(@RequestBody UserLoginRequest request){
        return loginServiceImp.login(request);
    }

    @ApiOperation(value = "手机登录")
    @PostMapping("/login/mobile")
    public JsonResult loginMobile(@RequestBody SmsLoginRequest request){
        return loginServiceImp.smsLogin(request);
    }

    @ApiOperation(value = "发送验证码")
    @PostMapping("/send/code")
    public JsonResult smsCode(@RequestBody SmsLoginRequest request) {
        return loginServiceImp.sendSms(request.getPhone());
    }

    @ApiOperation(value = "user测试")
    @PostMapping("/user/login")
    public JsonResult user(){
        return JsonResult.OK();
    }

    @ApiOperation(value = "admin测试")
    @PostMapping("/admin/login")
    public JsonResult admin(){
        return JsonResult.OK();
    }

    @ApiOperation(value = "stu测试")
    @PostMapping("/stu/login")
    public JsonResult stu(){
        return JsonResult.OK();
    }

    @ApiOperation(value = "all测试")
    @PostMapping("/all/login")
    public JsonResult all(){
        return JsonResult.OK();
    }


    @ApiOperation(value = "通过用户名查找")
    @GetMapping("/byname")
    public JsonResult getUserByName(@RequestParam("name") String name){
        return service.getUser(name);
    }

    @ApiOperation(value = "查询用户列表")
    @GetMapping("/getAll")
    public JsonResult getAll(){
        return service.getAll();
    }

    @ApiOperation(value = "模拟分布式锁")
    @GetMapping("/getOrderAndBuy")
    public JsonResult getOrderAndBuy(@RequestParam("goodsId")String goodsId)  {
        return service.getOrderAndBuy(goodsId);
    }
}
