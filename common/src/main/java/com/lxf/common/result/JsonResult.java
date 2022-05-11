package com.lxf.common.result;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.lxf.common.enums.ResultCode;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName : JsonResult
 * @Description mvc全局返回
 * @Date 2022/3/24 23:58
 * @Created lxf
 */
@Data
public class JsonResult<T>  implements Serializable {

    private long code;

    private String message;

    private T data;

    public JsonResult() {
    }

    public JsonResult(long code) {
        this.setCode(code);
    }

    public JsonResult(long code, String message) {
        this.setCode(code);
        this.setMessage(message);
    }

    public JsonResult(long code, String message, T data) {
        this.setCode(code);
        this.setMessage(message);
        this.setData(data);

    }

    public JsonResult(long code, T data) {
        this.setCode(code);
        this.setData(data);
    }

    public static JsonResult  OK(){
        JsonResult jsonResult = instance();
        jsonResult.setCode(ResultCode.SUCCESS.code());
        jsonResult.setMessage(ResultCode.SUCCESS.msg());
        return jsonResult;
    }

    public static<T> JsonResult  success(T data){
        JsonResult jsonResult = instance();
        jsonResult.setCode(ResultCode.SUCCESS.code());
        jsonResult.setMessage(ResultCode.SUCCESS.msg());
        jsonResult.setData(data);
        return jsonResult;
    }

    public static JsonResult  error(long code,String msg){
        JsonResult jsonResult = instance();
        jsonResult.setCode(code);
        jsonResult.setMessage(msg);
        return jsonResult;
    }

    public static<T> JsonResult  of(long code,String msg,T data){
        JsonResult jsonResult = instance();
        jsonResult.setCode(code);
        jsonResult.setMessage(msg);
        jsonResult.setData(data);
        return jsonResult;
    }

    public static JsonResult instance(){
        return new JsonResult();
    }

    public String toString() {
        JSONObject json = new JSONObject();
        try {
            json.put("code", code);
            json.put("message", message);
            json.put("data", data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json.toString();
    }

}
