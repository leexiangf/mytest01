package com.lxf.ums.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.lxf.ums.pojo.entity.SysRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @ClassName : SysRole
 * @Description 用户菜单
 * @Date 2022/4/10 12:02
 * @Created lxf
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class SysMenu {

    private Integer id;

    private String menuId;

    private String menuUrl;

    private List<SysRole> roles;
}
