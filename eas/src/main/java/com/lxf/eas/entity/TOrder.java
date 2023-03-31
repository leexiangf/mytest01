package com.lxf.eas.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Classname TOrder
 * @Description
 * @Date 2022/6/30 17:34
 * @Author lxf
 */
@TableName("t_order")
@Data
@Accessors(chain = true)
public class TOrder {

    private Long id;

    private Integer userId;

    private Integer orderId;

    private String cloumn;

    private Long createTime;
}
