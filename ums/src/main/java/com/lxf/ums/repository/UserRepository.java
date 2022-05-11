package com.lxf.ums.repository;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.lxf.ums.mapper.UserMapper;
import com.lxf.ums.pojo.entity.SysMenu;
import com.lxf.ums.pojo.entity.SysRole;
import com.lxf.ums.pojo.entity.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @ClassName : UserRepository
 * @Description TODO
 * @Date 2022/4/9 20:24
 * @Created lxf
 */
@Repository
public class UserRepository  {

    @Autowired
    UserMapper userMapper;

    public User selectByUserName(String username){
        return userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUsername,username));
    }

    public List<SysRole> getRoleByUserId( String userId){
        return userMapper.getRoleByUserId(userId);
    };

    public List<SysMenu> getMenuByUserId(String userId){
        return userMapper.getMenuByUserId(userId);
    };

    public List<SysMenu> getMenus(){
        return userMapper.getMenus();
    };

    public User selectByMobile(String mobile) {
        return userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getPhone,mobile));
    }

}
