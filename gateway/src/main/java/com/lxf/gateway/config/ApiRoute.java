package com.lxf.gateway.config;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @author
 */
@Data
@ToString
@Accessors(chain = true)
public class ApiRoute {

    private String id;
    private String pattern;
    private String uri;
}
