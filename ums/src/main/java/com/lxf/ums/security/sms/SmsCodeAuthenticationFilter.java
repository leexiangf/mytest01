package com.lxf.ums.security.sms;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.Assert;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ClassName : SmsCodeAuthenticationFilter
 * @Description TODO
 * @Date 2022/4/14 21:50
 * @Created lxf
 */
public class SmsCodeAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private static final AntPathRequestMatcher DEFAULT_ANT_PATH_REQUEST_MATCHER =
            new AntPathRequestMatcher("/sms/login", "POST");
    private String mobileParameter = "mobile";
    private String codeParameter = "code";

    private boolean postOnly = true;
    protected SmsCodeAuthenticationFilter() {
        super(DEFAULT_ANT_PATH_REQUEST_MATCHER);
    }
    protected SmsCodeAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(DEFAULT_ANT_PATH_REQUEST_MATCHER,authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        if (this.postOnly && !request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }
        String mobile = request.getParameter("mobile");
        mobile = (mobile!=null)?mobile.trim():"";
        String code = request.getParameter("code");
        code = (code!=null)?code.trim():"";
        SmsCodeAuthenticationToken smsCodeAuthenticationToken = new SmsCodeAuthenticationToken(mobile,code);
        setDetails(request,smsCodeAuthenticationToken);
        return this.getAuthenticationManager().authenticate(smsCodeAuthenticationToken);
    }

    protected String obtainMobile(HttpServletRequest request) {
        return request.getParameter(mobileParameter);
    }

    protected void setDetails(HttpServletRequest request, SmsCodeAuthenticationToken authRequest) {
        authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
    }

    public String getMobileParameter() {
        return mobileParameter;
    }

    public void setMobileParameter(String mobileParameter) {
        Assert.hasText(mobileParameter, "mobileParameter parameter must not be empty or null");
        this.mobileParameter = mobileParameter;
    }

    public String getCodeParameter() {
        Assert.hasText(codeParameter, "codeParameter parameter must not be empty or null");
        return codeParameter;
    }

    public void setCodeParameter(String codeParameter) {
        this.codeParameter = codeParameter;
    }

    public void setPostOnly(boolean postOnly) {
        this.postOnly = postOnly;
    }

}
