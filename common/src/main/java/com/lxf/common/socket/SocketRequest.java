package com.lxf.common.socket;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Classname SocketRequest
 * @Description
 * @Date 2022/8/22 14:35
 * @Author lxf
 */
@Data
public class SocketRequest<T> {
    @ApiModelProperty(value = "消息id")
    private String id;
    @ApiModelProperty(value = "请求方式",example = "request")
    private String mode ;
    @ApiModelProperty(value = "请求方法")
    private String method;
    @ApiModelProperty(value = "请求实体")
    private T data;

}
