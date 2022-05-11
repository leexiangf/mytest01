package com.lxf.ums.security.handler;

import com.alibaba.fastjson.JSON;
import com.lxf.common.result.JsonResult;
import com.lxf.ums.utils.WebUtil;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ClassName : AuthenticationEntryPointHandler
 * @Description  认证异常处理
 * @Date 2022/3/27 18:32
 * @Created lxf
 */
@Component
public class AuthenticationEntryPointHandler implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        JsonResult result = new JsonResult<>(HttpStatus.UNAUTHORIZED.value(), "用户认证请求失败请重新登录");
        String jsonString = JSON.toJSONString(result);
        //处理异常
        WebUtil.renderString(httpServletResponse,jsonString);
    }
}
