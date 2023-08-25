package com.lxf.common.exception;

import lombok.Getter;

/**
 * 响应码枚举，参考HTTP状态码的语义
 */
@Getter
public enum ResponseCodes {

    /**
     * 返回状态码和信息
     */
    SUCCESS("200", "成功"),
    FAIL("400", "失败"),
    FORBID("403", "禁止访问"),
    INTERNAL_SERVER_ERROR("500", "服务器内部错误"),
    SYSTEM_ERROR("600", "网络异常,稍后再试"),
    PARAM_ERROR("607", "请求数据错误"),
    PARAM_FORMAT_ERROR("608", "参数格式错误"),
    POWER_FAILD("609", "操作失败：权限不足"),
    SEND_PHONEMSG_FAILD("615", "短信验证码发送失败"),
    AUTH_CODE_ERROR("616", "验证码错误"),
    USER_ERROR("645", "用户错误"),
    USER_NOT_FOUND("647", "未查询到该用户信息"),
    PASSWORD_ERROR("648", "密码错误"),
    USER_EXIST("650", "此手机号已注册"),

;

    private String code;
    private String msg;

    ResponseCodes(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
     * 获取Code对象标准的JSON字符串
     **/
    public String toJSON() { return toJSON(msg); }

    public String toJSON(String msg) {
        return "{\"code\":\"" + code + "\",\"msg\":\"" + msg + "\"}";
    }


}