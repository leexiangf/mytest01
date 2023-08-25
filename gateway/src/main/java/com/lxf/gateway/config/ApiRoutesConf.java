package com.lxf.gateway.config;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @blame chb
 * @author chb
 * @date 2019/11/29 15:55
 */
@Component
@ConfigurationProperties(prefix = "routes")
@Data
@Accessors(chain = true)
public class ApiRoutesConf {

    private List<ApiRoute> apiRoute = new ArrayList<>();
}
