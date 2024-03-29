package com.lxf.eas.config;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @Classname LogFilterConfig
 * @Description
 * @Date 2024/3/29 15:35
 * @Author lxf
 */
@Component
public class LogFilterConfig {


    public Collection<Object> getRequest() {
        return new ArrayList<>();
    }

    public Collection<Object> getResponse() {
        return new ArrayList<>();
    }
}
