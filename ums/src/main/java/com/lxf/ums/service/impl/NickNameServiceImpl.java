package com.lxf.ums.service.impl;

import com.lxf.common.entity.SysNickName;
import com.lxf.ums.mapper.SysNickNameMapper;
import com.lxf.ums.service.NickNameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @ClassName : NickNameServiceImpl
 * @Description TODO
 * @Date 2022/4/27 22:01
 * @Created lxf
 */
@Service
public class NickNameServiceImpl implements NickNameService {

    @Autowired
    private SysNickNameMapper sysNickNameMapper;

    @Override
    public void test() {
        insertOne();
        insertTwo();
    }

    private boolean insertOne(){
        SysNickName nickName = new SysNickName();
        nickName.setNickName("张三丰");
        sysNickNameMapper.insert(nickName);
        return true;
    }

    private boolean insertTwo(){
        SysNickName nickName = new SysNickName();
        nickName.setNickName("王重阳");
        System.out.println(1/0);
        sysNickNameMapper.insert(nickName);
        return true;
    }
}
