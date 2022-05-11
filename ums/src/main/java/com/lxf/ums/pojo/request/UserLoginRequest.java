package com.lxf.ums.pojo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

/**
 * @ClassName : UserLoginRequest
 * @Description
 * @Date 2022/4/9 20:03
 * @Created lxf
 */
@ApiModel(value = "用户登录请求")
@Data
public class UserLoginRequest {

    @ApiModelProperty(value = "用户登录名")
    private String username;
    @ApiModelProperty(value = "用户登录密码")
    private String password;
}
