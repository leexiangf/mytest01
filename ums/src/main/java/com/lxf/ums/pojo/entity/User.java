package com.lxf.ums.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@TableName("sys_user")
public class User implements Serializable {
    @TableId(value = "id",type = IdType.AUTO)
    private int id;
    @TableField(value = "user_id")
    private String userId;
    @TableField(value = "name")
    private String name;
    @TableField(value = "age")
    private int age;
    @TableField(value = "username")
    private String username;
    @TableField(value = "password")
    private String password;
    @TableField(value = "phone")
    private String phone;
    @TableField(value = "openid")
    private String openid;
    @TableField(value = "nick_name")
    private String nickName;
    @TableField(value = "sex")
    private String sex;
    @TableField(value = "city")
    private String city;
    @TableField(value = "urlimg")
    private String urlimg;

}
