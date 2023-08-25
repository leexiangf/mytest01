package com.lxf.eas.utils;

import com.alibaba.fastjson2.JSONObject;
import com.lxf.common.annotation.RedissonLimit;
import com.lxf.common.enums.LimitType;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

/**
 * @Classname ProceedingJoinPointUtil
 * @Description
 * @Date 2023/7/27 15:24
 * @Author lxf
 */
@Component
public class ProceedingJoinPointUtil {
    @Autowired
    private HttpServletRequest request;

    private Map<LimitType, Function<ProceedingJoinPoint, String>> functionMap = new HashMap<>(10);

    @PostConstruct
    void initMap() {
        //初始化策略
        functionMap.put(LimitType.METHOD, this::getMethodTypeKey);
        functionMap.put(LimitType.PARAMS, this::getParamsTypeKey);
        functionMap.put(LimitType.USER, this::getUserTypeKey);
        functionMap.put(LimitType.IP, proceedingJoinPoint ->
                getIPAddress(request));
        functionMap.put(LimitType.REQUEST_URI, proceedingJoinPoint ->
                request.getRequestURI());
        functionMap.put(LimitType.REQUEST_URI_USERID, proceedingJoinPoint ->
                request.getRequestURI() + getUserId(request));
        functionMap.put(LimitType.REQUEST_URI_PARAMS, proceedingJoinPoint ->
                request.getRequestURI() + getParams(proceedingJoinPoint));
        functionMap.put(LimitType.REQUEST_URI_PARAMS_USERID, proceedingJoinPoint ->
                request.getRequestURI() + getParams(proceedingJoinPoint) + getUserId(request));
        functionMap.put(LimitType.SINGLE_USER, (proceedingJoinPoint) ->
                String.valueOf(getUserId(request)));
        functionMap.put(LimitType.SINGLE_METHOD, (proceedingJoinPoint -> {
            StringBuilder sb = new StringBuilder();
            appendMethodName(proceedingJoinPoint, sb);
            return sb.toString();
        }));
    }

    /**
     * 根据类型获取最终Key
     */
    public Object getKey(ProceedingJoinPoint joinPoint, RedissonLimit redisLimit) {
        //根据限制类型生成key
        Object generateKey = "";

        if (redisLimit.type() != LimitType.CUSTOM) {
            generateKey = generateKey(redisLimit.type(), joinPoint);
        } else {
            //自定义
            generateKey = redisLimit.key();
        }
        return generateKey;
    }

    /**
     * 根据LimitType生成key
     */
    private Object generateKey(LimitType type, ProceedingJoinPoint joinPoint) {
        Function<ProceedingJoinPoint, String> function = functionMap.get(type);
        return function.apply(joinPoint);
    }

    /**
     * 通过用户的userId进行访问评率限制
     */
    private String getUserId(HttpServletRequest request) {
        return  null;
    }


    /**
     * 方法级别
     * key = ClassName+MethodName
     */
    private String getMethodTypeKey(ProceedingJoinPoint joinPoint) {
        StringBuilder sb = new StringBuilder();
        appendMethodName(joinPoint, sb);
        return sb.toString();
    }


    /**
     * 参数级别
     * key = ClassName+MethodName+Params
     */
    private String getParamsTypeKey(ProceedingJoinPoint joinPoint) {
        StringBuilder sb = new StringBuilder();
        appendMethodName(joinPoint, sb);
        appendParams(joinPoint, sb);
        return sb.toString();
    }


    /**
     * 用户级别
     * key = ClassName+MethodName+Params+UserId
     */
    private String getUserTypeKey(ProceedingJoinPoint joinPoint) {
        StringBuilder sb = new StringBuilder();
        appendMethodName(joinPoint, sb);
        appendParams(joinPoint, sb);
        //获取userId
        sb.append(getUserId(request));
        return sb.toString();
    }


    /**
     * StringBuilder添加类名和方法名
     */
    private void appendMethodName(ProceedingJoinPoint joinPoint, StringBuilder sb) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        //类名
        sb.append(joinPoint.getTarget().getClass().getName())
                //方法名
                .append(method.getName());
    }

    /**
     * StringBuilder添加方法参数值
     */
    private void appendParams(ProceedingJoinPoint joinPoint, StringBuilder sb) {
        for (Object o : joinPoint.getArgs()) {
            sb.append(o.toString());
        }
    }

    private String getParams(ProceedingJoinPoint joinPoint) {
        StringBuilder sb = new StringBuilder();
        for (Object o : joinPoint.getArgs()) {
            if (o instanceof MultipartFile) {
                System.out.println("MultipartFile输入流");
            } else {
                sb.append(o.toString());
            }
        }
        return sb.toString();
    }


    private String getIPAddress(HttpServletRequest request) {
        String ip = null;

        //X-Forwarded-For：Squid 服务代理
        String ipAddresses = request.getHeader("X-Forwarded-For");

        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            //Proxy-Client-IP：apache 服务代理
            ipAddresses = request.getHeader("Proxy-Client-IP");
        }

        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            //WL-Proxy-Client-IP：weblogic 服务代理
            ipAddresses = request.getHeader("WL-Proxy-Client-IP");
        }

        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            //HTTP_CLIENT_IP：有些代理服务器
            ipAddresses = request.getHeader("HTTP_CLIENT_IP");
        }

        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            //X-Real-IP：nginx服务代理
            ipAddresses = request.getHeader("X-Real-IP");
        }

        //有些网络通过多层代理，那么获取到的ip就会有多个，一般都是通过逗号（,）分割开来，并且第一个ip为客户端的真实IP
        if (ipAddresses != null && ipAddresses.length() != 0) {
            ip = ipAddresses.split(",")[0];
        }

        //还是不能获取到，最后再通过request.getRemoteAddr();获取
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            ip = request.getRemoteAddr();
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)){
            ip = "defaultIp";
        }
        return ip;
    }
}