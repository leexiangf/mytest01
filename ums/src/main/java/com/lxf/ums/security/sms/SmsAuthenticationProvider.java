package com.lxf.ums.security.sms;

import com.lxf.ums.security.sms.SmsCodeAuthenticationToken;
import com.lxf.ums.utils.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.Objects;

/**
 * @ClassName : SmsAuthenticationProvider
 * @Description TODO
 * @Date 2022/4/14 22:00
 * @Created lxf
 */
@Component
public class SmsAuthenticationProvider implements AuthenticationProvider {

    protected MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();


    private UserDetailsService userDetailsService;

    private RedisUtil redisUtil;

    public SmsAuthenticationProvider(@Qualifier(value = "MobileUserDetailService")UserDetailsService userDetailsService, RedisUtil redisUtil) {
        this.userDetailsService = userDetailsService;
        this.redisUtil = redisUtil;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Assert.isInstanceOf(SmsCodeAuthenticationToken.class, authentication,
                () -> this.messages.getMessage("AbstractUserDetailsAuthenticationProvider.onlySupports",
                        "Only SmsCodeAuthenticationToken is supported"));
        SmsCodeAuthenticationToken token = (SmsCodeAuthenticationToken) authentication;
        String mobile = (String) token.getPrincipal();
        String code = (String) token.getCredentials();
        String sendCode =null;
        try {
             sendCode = (String) redisUtil.get(mobile);
        }catch (Exception e){
            throw new BadCredentialsException("验证码过期");
        }
        if(StringUtils.isBlank(sendCode)){
            throw new BadCredentialsException("验证码过期");
        }
        if(!StringUtils.equals(code,sendCode)){
            throw new BadCredentialsException("验证码错误");
        }
        UserDetails userDetails = userDetailsService.loadUserByUsername(mobile);
        if(Objects.isNull(userDetails)){
            throw new InternalAuthenticationServiceException("当前手机用户未查找");
        }
        SmsCodeAuthenticationToken authenticationToken =
                new SmsCodeAuthenticationToken(userDetails, userDetails.getAuthorities());
        authenticationToken.setDetails(token.getDetails());
        return authenticationToken;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return SmsAuthenticationProvider.class.isAssignableFrom(aClass);
    }
}
