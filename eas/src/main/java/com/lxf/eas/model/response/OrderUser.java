package com.lxf.eas.model.response;

import lombok.Data;

/**
 * @Classname OrderUser
 * @Description
 * @Date 2022/7/1 15:26
 * @Author lxf
 */
@Data
public class OrderUser {

    private Long id;

    private Integer userId;

    private Integer orderId;

    private String cloumn;

    private Long createTime;

    private String name;

    private String userName;
}
