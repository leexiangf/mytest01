package com.lxf.ums.security.filter;

import com.lxf.ums.enums.SysRoleCode;
import com.lxf.ums.pojo.entity.SysMenu;
import com.lxf.ums.pojo.entity.SysRole;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.SecurityConfig;
import com.lxf.ums.security.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName : MyFilterInvocationSecurityMetadataSource
 * @Description  权限过滤
 * @Date 2022/4/10 18:48
 * @Created lxf
 */

@Slf4j
@Component
public class MyFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

    @Autowired
    private MenuService menuService;

    /**
     * 路径匹配器
     */
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    /**
     * 匹配路径，返回角色列表 (如果这个请求url含有对应角色才能访问，返回对应角色)
     * @param object
     * @return
     * @throws IllegalArgumentException
     */
    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        //获取请求路径
        String requestUrl = ((FilterInvocation) object).getRequestUrl();
        log.info("requestUrl : {}",requestUrl);
        //查询数据库 获取所有菜单
        List<SysMenu> menus = menuService.getMenus();

        for (SysMenu menu : menus) {
            //如果路径匹配
            if(antPathMatcher.match(menu.getMenuUrl(),requestUrl)){
                //获取对应角色
                List<SysRole> roles = menu.getRoles();
                String[] roleNames = new String[roles.size()];
                for (int i = 0; i < roles.size(); i++) {
                    roleNames[i] =roles.get(i).getRoleName();
                }
                log.info("对应路径查询到的角色:{}", Arrays.toString(roleNames));
              return   SecurityConfig.createList(roleNames);
            }
        }
        //设置默认访问角色 游客模式
       return SecurityConfig.createList(SysRoleCode.ROLE_GUEST.roleName());
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }
}
