package com.lxf.mytest01.mapper;


import com.lxf.mytest01.pojo.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


public interface UserMapper {
    /**
     * 获取所有user
     * @return
     */
    public List<User> getAll();
}
