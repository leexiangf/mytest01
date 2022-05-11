package com.lxf.ums.security.service;

import com.lxf.ums.pojo.entity.SysMenu;
import com.lxf.ums.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName : MenuService
 * @Description
 * @Date 2022/4/10 13:28
 * @Created lxf
 */
@Service
public class MenuService {

    @Autowired
    private UserRepository userRepository;

    public List<SysMenu> getMenus(){
        return userRepository.getMenus();
    }
}
