package com.lxf.eas.controller;

import com.lxf.common.result.JsonResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

/**
 * @ClassName : TestController
 * @Description TODO
 * @Date 2022/4/17 15:03
 * @Created lxf
 */
@RestController
@RequestMapping("/test")
public class TestController {



    @GetMapping("/mytest")
    public JsonResult test() throws UnsupportedEncodingException {


        return JsonResult.OK();
    }



}
