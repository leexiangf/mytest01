package com.lxf.ums.model.template;

/**
 * @Classname Template
 * @Description
 * @Date 2022/8/10 16:45
 * @Author lxf
 */
public abstract class Template {
    abstract void step1();
    abstract void step2();
    abstract void step3();

    public final void doit(){
        System.out.println("go");
        step1();
        step2();
        step3();
        System.out.println("over");
    }
}
