package com.lxf.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @ClassName : SysNickName
 * @Description
 * @Date 2022/4/27 21:46
 * @Created lxf
 */
@TableName("sys_nick_name")
@Data
@ToString
@Accessors(chain = true)
public class SysNickName {
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    @TableField("nick_name")
    private String nickName;
}
