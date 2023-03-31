package com.lxf.ums.model.strategy;

/**
 * @Classname Running
 * @Description
 * @Date 2022/8/10 16:03
 * @Author lxf
 */
public class Running implements Sports{
    @Override
    public void sportTime() {
        System.out.println("跑步三十分钟");
    }
}
