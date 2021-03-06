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
        //?????????????????? user/login   anonymous()??????????????????????????????
        http.authorizeRequests()
                .antMatchers("/login","/sms/login").anonymous()
                //permitAll() ??????????????????????????????????????????
                .antMatchers("/index","/sms/send/code").permitAll()
                //????????????????????????????????????
                .and().authorizeRequests()
                //.accessDecisionManager(myAccessDecisionManager)
                //????????????
                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                    @Override
                    public <O extends FilterSecurityInterceptor> O postProcess(O object) {
                        object.setAccessDecisionManager(myAccessDecisionManager);
                        object.setSecurityMetadataSource(myFilterInvocationSecurityMetadataSource);
                        return object;
                    }
                })
                .anyRequest().authenticated();
        //????????????????????????????????????
        http.apply(smsSecurityConfigurerAdapter);
        //?????????????????????login?????????UsernamePasswordAuthenticationFilter
        //http.formLogin().loginPage("/login");
        //csrf              ??????session?????????securityContext
        http.csrf().disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        //?????? ???????????????
        http.logout().logoutUrl("/");
        //???????????????
        http.rememberMe();
        //???????????????jwt????????? ?????? UsernamePasswordAuthenticationFilter ????????????
        //http.addFilterBefore(jwtAuthenticationTokenFilter, WebAsyncManagerIntegrationFilter.class);
        http.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
        http.exceptionHandling()
                //????????????????????????
                .authenticationEntryPoint(authenticationEntryPointHandler)
                //????????????????????????
                .accessDeniedHandler(accessDeniedHandlerImp);
        //????????????
        http.cors();
    }
    //??????
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
       //auth.userDetailsService(userDetailServiceImp);
        super.configure(auth);
    }


    /**
     * ???Provider???????????????????????????
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
        //????????????????????????????????????????????? swagger??????
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
        daoAuthenticationProvider.setHideUserNotFoundExceptions(false); // ?????????????????????????????????
        return daoAuthenticationProvider;
    }


}
