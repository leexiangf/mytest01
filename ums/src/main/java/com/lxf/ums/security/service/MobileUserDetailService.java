package com.lxf.ums.security.service;

import com.lxf.ums.pojo.entity.SysRole;
import com.lxf.ums.pojo.entity.User;
import com.lxf.ums.repository.UserRepository;
import com.lxf.ums.security.userDetail.LoginUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @ClassName : MobileUserDetailService
 * @Description TODO
 * @Date 2022/4/14 22:17
 * @Created lxf
 */
@Slf4j
@Service("MobileUserDetailService")
public class MobileUserDetailService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String mobile) throws UsernameNotFoundException {
        User user = userRepository.selectByMobile(mobile);
        if(Objects.isNull(user)){
            throw new UsernameNotFoundException("用户未找到");
        }
        LoginUser loginUser = new LoginUser();
        loginUser.setUser(user);
        //封装权限信息 这里从数据库通过userId查询出来的权限集合
        List<SysRole> roles = userRepository.getRoleByUserId(user.getUserId());
        List<String> collect = roles.stream().map(SysRole::getRoleName).collect(Collectors.toList());
        for (String s : collect) {
            log.info("用户角色名：{}",s);
        }
        List<SimpleGrantedAuthority> authorities =
                collect.stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());
        loginUser.setAuthorities(authorities);
        return loginUser;
    }
}
