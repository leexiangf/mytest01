package com.lxf.ums.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lxf.ums.pojo.entity.SysMenu;
import com.lxf.ums.pojo.entity.SysRole;
import com.lxf.ums.pojo.entity.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface UserMapper extends BaseMapper<User> {
    /**
     * 获取所有user
     * @return
     */
    public List<User> getAll();

    /**
     * 通过用户名查找用户信息
     * @param username
     * @return
     */
    public List<SysRole> getRoleByUserId(@Param("userId") String userId);

    public List<SysMenu> getMenuByUserId(@Param("userId")String userId);

    public List<SysMenu> getMenus();
}
