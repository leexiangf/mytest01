package com.lxf.ums.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @ClassName : SysRole
 * @Description 用户角色表
 * @Date 2022/4/10 12:02
 * @Created lxf
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@TableName("sys_role")
public class SysRole {
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    @TableField(value = "role_id")
    private String roleId;
    @TableField(value = "role_name")
    private String roleName;
}
