package com.lxf.ums.pojo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName : UserLoginRequest
 * @Description
 * @Date 2022/4/9 20:03
 * @Created lxf
 */
@ApiModel(value = "手机登录请求")
@Data
public class SmsLoginRequest {

    @ApiModelProperty(value = "手机号")
    private String phone;
    @ApiModelProperty(value = "验证码")
    private String code;
}
