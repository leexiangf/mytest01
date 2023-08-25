package com.lxf.common.enums;

/**
 * @Classname LimitType
 * @Description
 * @Date 2023/7/27 15:16
 * @Author lxf
 */
public enum LimitType {

    /**
     * 自定义key
     */
    CUSTOM,

    /**
     * 请求者IP
     */
    IP,

    /**
     * 方法级别限流
     * key = ClassName+MethodName
     */
    METHOD,

    /**
     * 参数级别限流
     * key = ClassName+MethodName+Params
     */
    PARAMS,

    /**
     * 用户级别限流
     * key = ClassName+MethodName+Params+UserId
     */
    USER,

    /**
     * 根据request的uri限流
     * key = Request_uri
     */
    REQUEST_URI,

    /**
     * 对requestUri+userId限流
     * key = Request_uri+UserId
     */
    REQUEST_URI_USERID,


    /**
     * 对userId限流
     * key = userId
     */
    SINGLE_USER,

    /**
     * 对方法限流
     * key = ClassName+MethodName
     */
    SINGLE_METHOD,

    /**
     * 对uri+params限流
     * key = uri+params
     */
    REQUEST_URI_PARAMS,

    /**
     * 对uri+params+userId限流
     * key = uri+params+userId
     */
    REQUEST_URI_PARAMS_USERID;

}
