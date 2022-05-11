package com.lxf.ums.security.filter;

import com.alibaba.druid.util.StringUtils;
import com.lxf.ums.enums.SysRoleCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.vote.UnanimousBased;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.expression.WebExpressionVoter;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * @ClassName : MyAccessDecisionManager
 * @Description 获取FilterInvocationSecurityMetadataSource传入的角色列表，来鉴权
 * @Date 2022/4/10 19:48
 * @Created lxf
 */
@Slf4j
@Component
public class MyAccessDecisionManager implements AccessDecisionManager {

    /**
     *
     * @param authentication 用户信息(UserDetailsService 获取到)
     * @param object
     * @param configAttributes 请求地址对应的角色信息(数据库查出的数据)
     * @throws AccessDeniedException
     * @throws InsufficientAuthenticationException
     */
    @Override
    public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException, InsufficientAuthenticationException {
        log.info("进入权限鉴定~");
        for (ConfigAttribute configAttribute : configAttributes) {
            //判断是否游客模式
            if(StringUtils.equals(SysRoleCode.ROLE_GUEST.roleName(),configAttribute.getAttribute())){
                //匿名用户
                if(configAttribute instanceof AnonymousAuthenticationToken){
                    log.error("匿名用户访问");
                    throw new AccessDeniedException("无访问权限");
                }else {
                    //已经登录的用户，不需要权限的访问地址 直接放行
                    return;
                }
            }
            log.info("用户鉴权查看信息：{}",authentication.getPrincipal());
            log.info("角色鉴权查看信息：{}",authentication.getAuthorities());
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            for (GrantedAuthority authority : authorities) {
                log.info("用户所拥有的角色 ------------------> : {}",authority.getAuthority());
                log.info("路径所需要的角色 ------------------> : {}",configAttribute.getAttribute());
                if(StringUtils.equals(authority.getAuthority(),configAttribute.getAttribute())){
                    //如果用户角色和路径所需角色相同，则放行
                    return;
                }
            }
        }
        throw new AccessDeniedException("无访问权限");
    }

/*    @Bean
    public AccessDecisionVoter<FilterInvocation> accessDecisionProcessor() {
        return new AccessDecisionProcessor();
    }

    @Bean
    public AccessDecisionManager accessDecisionManager() {
        // 构造一个新的AccessDecisionManager 放入两个投票器
        List<AccessDecisionVoter<?>> decisionVoters = Arrays.asList(new WebExpressionVoter(), accessDecisionProcessor());
        return new UnanimousBased(decisionVoters);
    }*/

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }
}
