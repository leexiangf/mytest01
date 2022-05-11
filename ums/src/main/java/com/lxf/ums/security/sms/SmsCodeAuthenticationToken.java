package com.lxf.ums.security.sms;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * @ClassName : SmsCodeAuthenticationToken
 * @Description  封装sms信息
 * @Date 2022/4/14 21:42
 * @Created lxf
 */
public class SmsCodeAuthenticationToken extends AbstractAuthenticationToken {

    private final Object principal;
    private Object credentials;

    public SmsCodeAuthenticationToken(Object mobile,Object code) {
        super((Collection)null);
        this.principal = mobile;
        this.credentials=code;
        this.setAuthenticated(false);
    }

    public SmsCodeAuthenticationToken(Object principal, Object credentials,Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.credentials=credentials;
        super.setAuthenticated(true);
    }

    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        if (isAuthenticated) {
            throw new IllegalArgumentException("Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        } else {
            super.setAuthenticated(false);
        }
    }

    public void eraseCredentials() {
        super.eraseCredentials();
    }

    @Override
    public Object getCredentials() {
        return this.credentials;
    }

    public Object getPrincipal() {
        return this.principal;
    }

}
