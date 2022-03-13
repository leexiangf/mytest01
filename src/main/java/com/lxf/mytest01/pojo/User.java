package com.lxf.mytest01.pojo;

import lombok.Data;

@Data
public class User {
    private int id;
    private String name;
    private int age;

    public User(int i, String zs, int i1) {
        this.id=i;
        this.name=zs;
        this.age=i1;
    }
}
