package com.lxf.ums.security.sms;

import com.lxf.ums.security.handler.AuthenticationFailureHandlerImp;
import com.lxf.ums.security.handler.AuthenticationSuccessHandlerImp;
import com.lxf.ums.security.service.MobileUserDetailService;
import com.lxf.ums.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.Resource;

/**
 * @ClassName : SmsSecurityConfigurerAdapter
 * @Description 自定义Adapter
 * @Date 2022/4/14 23:12
 * @Created lxf
 */
@Configuration
public class SmsCodeSecurityConfigurerAdapter extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {


    @Autowired
    AuthenticationSuccessHandlerImp authenticationSuccessHandler;

    @Autowired
    AuthenticationFailureHandlerImp authenticationFailureHandler;

    @Autowired
    private RedisUtil redisUtil;

    @Resource
    private MobileUserDetailService mobileUserDetailService;
    @Override
    public void configure(HttpSecurity http) throws Exception {
        SmsCodeAuthenticationFilter smsAuthenticationFilter = new SmsCodeAuthenticationFilter();
        smsAuthenticationFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
        smsAuthenticationFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
        smsAuthenticationFilter.setAuthenticationFailureHandler(authenticationFailureHandler);
        SmsAuthenticationProvider smsAuthenticationProvider = new SmsAuthenticationProvider(mobileUserDetailService,redisUtil);
        http.authenticationProvider(smsAuthenticationProvider)
                .addFilterAfter(smsAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }


}
