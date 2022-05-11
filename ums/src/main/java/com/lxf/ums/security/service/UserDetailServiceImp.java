package com.lxf.ums.security.service;

import com.lxf.ums.mapper.UserMapper;
import com.lxf.ums.pojo.entity.SysMenu;
import com.lxf.ums.pojo.entity.SysRole;
import com.lxf.ums.pojo.entity.User;
import com.lxf.ums.repository.UserRepository;
import com.lxf.ums.security.userDetail.LoginUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @ClassName : UserDetailServiceImp
 * @Description  封装自己的信息类  （UserDetailsService 是security自己的用户信息）
 * @Date 2022/3/27 14:45
 * @Created lxf
 */
@Slf4j
@Service
public class UserDetailServiceImp implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("username:{}",username);
        User user = userRepository.selectByUserName(username);
        if(Objects.isNull(user)){
            throw new UsernameNotFoundException("用户名或密码错误");
        }
        //封装权限信息 这里从数据库通过userId查询出来的权限集合
        List<SysRole> roles = userRepository.getRoleByUserId(user.getUserId());
        List<String> collect = roles.stream().map((role) -> {
            return role.getRoleName();
        }).collect(Collectors.toList());
        for (String s : collect) {
            log.info("用户角色名：{}",s);
        }
        LoginUser loginUser = new LoginUser();
        loginUser.setUser(user);
        List<SimpleGrantedAuthority> authorities =
                collect.stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());
        loginUser.setAuthorities(authorities);

        return loginUser;
    }
}
