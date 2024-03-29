package com.lxf.eas.aspect;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lxf.eas.config.LogFilterConfig;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Aspect
@Component
@Slf4j
public class LogAspect {

    private static ObjectMapper objectMapper = new ObjectMapper();
    private ThreadLocal<SpendTime> spendTimeThreadLocal = new ThreadLocal<>();
    @Autowired
    private LogFilterConfig logFilterConfig;
    /**
     * 定义切点
     * execution([权限修饰符] [返回值类型] [简单类名/全类名] [方法名]([参数列表]))
     */
    @Pointcut("execution (public * com.promotion.eas.controller..*.*(..))")
    public void log() {
    }

    /**
     * 请求方法前打印内容
     *
     * @param joinPoint
     */
    @Before("log()")
    public void doBefore(JoinPoint joinPoint) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String method = request.getMethod();
        StringBuilder params = new StringBuilder();
        String requestURI = request.getRequestURI();
        String authorization = request.getHeader("Authorization");
        SpendTime spendTime = new SpendTime();
        spendTime.setStartTime(Instant.now().toEpochMilli());
        spendTime.setUrl(requestURI);
        spendTimeThreadLocal.set(spendTime);
        if(logFilterConfig.getRequest().contains(requestURI)){
            log.info("RECV:traceId={} {} {} ", spendTime.getTraceId(), requestURI, authorization);
            return;
        }
        if (HttpMethod.GET.toString().equals(method)) {
            String queryString = request.getQueryString();
            if (Objects.nonNull(queryString)) {
                try {
                    params.append(URLDecoder.decode(queryString, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    log.warn("URL解码失败");
                }
            }
        } else { //其他请求
            Object[] paramsArray = joinPoint.getArgs();
            if (paramsArray != null && paramsArray.length > 0) {
                for (int i = 0; i < paramsArray.length; i++) {
                    if (paramsArray[i] instanceof Serializable) {
                        params.append(paramsArray[i].toString()).append(",");
                        continue;
                    }

                    //使用json系列化 反射等等方法 反系列化会影响请求性能建议重写tostring方法实现系列化接口
                    if (paramsArray[i] instanceof HttpServletRequest) {
                        continue;
                    }
                    if (paramsArray[i] instanceof HttpServletResponse) {
                        continue;
                    }
                    try {
                        String param = objectMapper.writeValueAsString(paramsArray[i]);
                        if (Objects.nonNull(param)) {
                            params.append(param).append(",");
                        }
                    } catch (Exception e) {
                        log.error("doBefore obj to json exception obj={},msg={}", paramsArray[i], e);
                    }
                }
            }
        }
        log.info("RECV:traceId={} {} {} {} {}", spendTime.getTraceId(), requestURI, method, params, authorization);
    }

    /**
     * 在方法执行后打印返回内容
     *
     * @param obj
     */
    @AfterReturning(returning = "obj", pointcut = "log()")
    public void doAfterReturning(Object obj) {
        SpendTime spendTime = spendTimeThreadLocal.get();
        long time = Instant.now().toEpochMilli() - spendTime.getStartTime();
        if(spendTime.getUrl()!= null && logFilterConfig.getResponse().contains(spendTime.getUrl())){
            log.info("OUT:traceId={} {} ms {}", spendTime.getTraceId(), time, spendTime.getUrl());
            spendTimeThreadLocal.remove();
            return;
        }
        if (obj != null) {
            String result = null;
            if (obj instanceof Serializable) {
                result = JSONObject.toJSONString(obj);
            } else {
                try {
                    result = objectMapper.writeValueAsString(obj);
                } catch (JsonProcessingException e) {
                    log.error("doAfter Returning obj to json exception obj={},msg={}", obj, e);
                }
            }
            log.info("OUT:traceId={} {}ms {} {}", spendTime.getTraceId(), time, spendTime.getUrl(), result);
        } else {
            log.info("OUT:traceId={} {}ms {}", spendTime.getTraceId(), time, spendTime.getUrl());
        }
        spendTimeThreadLocal.remove();
    }


    @Data
    public class SpendTime {
        private String traceId = UUID.randomUUID().toString().replace("-", "");
        private String url;
        private long startTime;
    }
}
