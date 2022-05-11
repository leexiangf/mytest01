package com.lxf.ums.security.config;

import com.lxf.ums.security.filter.JwtAuthenticationTokenFilter;
import com.lxf.ums.security.filter.MyAccessDecisionManager;
import com.lxf.ums.security.filter.MyFilterInvocationSecurityMetadataSource;
import com.lxf.ums.security.handler.AccessDeniedHandlerImp;
import com.lxf.ums.security.handler.AuthenticationEntryPointHandler;
import com.lxf.ums.security.handler.AuthenticationSuccessHandlerImp;
import com.lxf.ums.security.service.UserDetailServiceImp;
import com.lxf.ums.security.sms.SmsAuthenticationProvider;
import com.lxf.ums.security.sms.SmsCodeSecurityConfigurerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @ClassName : SecurityConfig
 * @Description
 * @Date 2022/3/21 19:44
 * @Created lxf
 */
@EnableWebSecurity(debug = true)
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    @Autowired
    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    @Autowired
    private AuthenticationEntryPointHandler authenticationEntryPointHandler;

    @Autowired
    private AccessDeniedHandlerImp accessDeniedHandlerImp;

    @Autowired
    private AuthenticationSuccessHandlerImp appLoginInSuccessHandler;

    @Autowired
    private MyAccessDecisionManager myAccessDecisionManager;

    @Autowired
    private MyFilterInvocationSecurityMetadataSource myFilterInvocationSecurityMetadataSource;

    @Autowired
    private UserDetailServiceImp userDetailServiceImp;

    @Autowired
    private SmsCodeSecurityConfigurerAdapter smsSecurityConfigurerAdapter;
    @Autowired
    SmsAuthenticationProvider smsAuthenticationProvider;


    @Bean
    protected PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //放行登录接口 user/login   anonymous()只能未登录的状态访问
        http.authorizeRequests()
                .antMatchers("/login","/sms/login").anonymous()
                //permitAll() 所有人能访问（登录和未登录）
                .antMatchers("/index","/sms/send/code").permitAll()
                //除上述外所有请求都要认证
                .and().authorizeRequests()
                //.accessDecisionManager(myAccessDecisionManager)
                //添加鉴权
                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                    @Override
                    public <O extends FilterSecurityInterceptor> O postProcess(O object) {
                        object.setAccessDecisionManager(myAccessDecisionManager);
                        object.setSecurityMetadataSource(myFilterInvocationSecurityMetadataSource);
                        return object;
                    }
                })
                .anyRequest().authenticated();
        //把自定义短信登录添加进去
        http.apply(smsSecurityConfigurerAdapter);
        //基于表单提交的login会进入UsernamePasswordAuthenticationFilter
        //http.formLogin().loginPage("/login");
        //csrf              关闭session获取到securityContext
        http.csrf().disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        //注销 并回到首页
        http.logout().logoutUrl("/");
        //记住我功能
        http.rememberMe();
        //把自定义的jwt过滤器 放在 UsernamePasswordAuthenticationFilter 之前执行
        //http.addFilterBefore(jwtAuthenticationTokenFilter, WebAsyncManagerIntegrationFilter.class);
        http.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
        http.exceptionHandling()
                //认证失败异常处理
                .authenticationEntryPoint(authenticationEntryPointHandler)
                //授权失败异常处理
                .accessDeniedHandler(accessDeniedHandlerImp);
        //允许跨域
        http.cors();
    }
    //认证
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
       //auth.userDetailsService(userDetailServiceImp);
        super.configure(auth);
    }


    /**
     * 将Provider添加到认证管理器中
     *
     * @return
     * @throws Exception
     */
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        ProviderManager authenticationManager =
                new ProviderManager(Arrays.asList(smsAuthenticationProvider, daoAuthenticationProvider()));
        authenticationManager.setEraseCredentialsAfterAuthentication(false);
        return authenticationManager;
    }


    @Override
    public void configure(WebSecurity web) throws Exception {
        //所需要用到的静态资源，允许访问 swagger相关
        web.ignoring().antMatchers( "/swagger-ui.html",
                "/swagger-ui/*",
                "/swagger-resources/**",
                "/v2/api-docs",
                "/v3/api-docs",
                "/webjars/**");
    }


    @Bean
    DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(userDetailServiceImp);
        daoAuthenticationProvider.setHideUserNotFoundExceptions(false); // 设置显示找不到用户异常
        return daoAuthenticationProvider;
    }


}
