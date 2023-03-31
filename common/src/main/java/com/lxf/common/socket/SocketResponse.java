package com.lxf.common.socket;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Classname SocketRequest
 * @Description
 * @Date 2022/8/22 14:35
 * @Author lxf
 */
@Data
@Accessors(chain = true)
public class SocketResponse<T> {
    @ApiModelProperty(value = "消息id")
    private String id;
    @ApiModelProperty(value = "响应模式",example = "response")
    private String mode = "response";
    @ApiModelProperty(value = "响应方法")
    private String method;
    @ApiModelProperty(value = "响应实体")
    private T resultData;

    public SocketResponse(String id, String method, T resultData) {
        this.id = id;
        this.method = method;
        this.resultData = resultData;
    }

    public SocketResponse() {
    }

    public static<T> SocketResponse data(String id, String method, T resultData){
        return new SocketResponse(id,method,resultData);
    }
}
