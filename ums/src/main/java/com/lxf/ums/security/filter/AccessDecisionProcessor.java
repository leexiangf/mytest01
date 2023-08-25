package com.lxf.ums.security.filter;

import com.github.benmanes.caffeine.cache.Cache;
import com.lxf.ums.pojo.entity.SysMenu;
import com.lxf.ums.security.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;
import org.springframework.util.AntPathMatcher;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName : AccessDecisionProcessor
 * @Description  自定义AccessDecisionVoter
 * @Date 2022/4/15 22:58
 * @Created lxf
 */
public class AccessDecisionProcessor implements AccessDecisionVoter<FilterInvocation> {

    @Autowired
    private MenuService menuService;

    /**
     * 路径匹配器
     */
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public int vote(Authentication authentication, FilterInvocation object, Collection<ConfigAttribute> attributes) {
        String requestUrl = object.getRequestUrl();
        List<SysMenu> menus = menuService.getMenus();
        for (SysMenu menu : menus) {
            if(antPathMatcher.match(menu.getMenuUrl(),requestUrl)){
                return ACCESS_GRANTED;
            }else {
                return ACCESS_ABSTAIN;
            }
        }
        return ACCESS_DENIED;
    }

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return false;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return false;
    }
}
