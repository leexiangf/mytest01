package com.lxf.ums.model.strategy;

/**
 * @Classname Swim
 * @Description
 * @Date 2022/8/10 16:02
 * @Author lxf
 */
public class Swim implements Sports{
    @Override
    public void sportTime() {
        System.out.println("游泳一小时");
    }
}
