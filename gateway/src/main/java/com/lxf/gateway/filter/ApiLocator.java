package com.lxf.gateway.filter;


import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.lxf.gateway.config.ApiRoute;
import com.lxf.gateway.config.ApiRoutesConf;
import com.lxf.gateway.util.GatewayHttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.GatewayFilterSpec;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.gateway.route.builder.UriSpec;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/***
 * @author
 */
@Component
@Slf4j
@RefreshScope
public class ApiLocator {

    @Autowired
    private ApiRoutesConf apiRoutesConf;


    private String[] noAuthorUrls = {
            "/wbs/doc.html",
            "/wbs/swagger-resources",
            "/wbs/webjars/**",
            "/wbs/v2/**",
    };

    private final String Authorization = "Authorization";

    /**
     * 启动加载路由
     *
     * @param builder
     * @return
     */
    @Bean("routeLocator")
    public RouteLocator routes(RouteLocatorBuilder builder) {
        RouteLocatorBuilder.Builder locatorBuilder = builder.routes();
        //循环设置路由
        List<ApiRoute> list = apiRoutesConf.getApiRoute();
        for (ApiRoute param : list) {
            //根据请求头类型设置不同的路由
            String contentType = HttpHeaders.CONTENT_TYPE;
            locatorBuilder.route(param.getId() + "_json",
                            spec -> spec.path(param.getPattern())
                                    .and()
                                    .order(30)
//                                    .header(contentType, ".*application/json.*")
                                    .method(HttpMethod.POST)
                                    .filters(filterPost())
                                    .uri(param.getUri()))
                    .route(param.getId() + "_file_upload",
                            spec -> spec.path(param.getPattern())
                                    .and()
                                    .order(20)
                                    .header(contentType, ".*multipart/form-data.*")
                                    .or()
                                    .header(contentType, ".*image/.*")
                                    .or()
                                    .header(contentType, ".*application/vnd.ms-excel.*")
                                    .or()
                                    .header(contentType, ".*multipart/mixed.*")
                                    .filters(fileUpload()).uri(param.getUri()))
                    .route(param.getId() + "_callback",
                            spec -> spec.path(param.getPattern())
                                    .and()
                                    .order(1)
                                    .path(noAuthorUrls)
                                    .uri(param.getUri()))
                    .route(param.getId() + "_get",
                            spec -> spec.path(param.getPattern())
                                    .and()
                                    .order(10)
                                    .method(HttpMethod.GET)
                                    .filters(filterGet())
                                    .uri(param.getUri()));
        }
        RouteLocator routeLocator = locatorBuilder.build();
        log.info("custom RouteLocator is loading ... {}", routeLocator.getRoutes());
        return routeLocator;
    }

    /**
     * file过滤器。
     *
     * @return
     */
    public Function<GatewayFilterSpec, UriSpec> filterGet() {
        return filterSpec -> filterSpec.filter(new GatewayFilter() {
            @Override
            public Mono<Void> filter(ServerWebExchange serverWebExchange, GatewayFilterChain chain) {
                String uri = serverWebExchange.getRequest().getURI().getPath();
                HttpMethod method = serverWebExchange.getRequest().getMethod();
                log.info("RECV:{} {} {} ", method, uri, serverWebExchange.getRequest().getHeaders());
                // 针对于oss进行放行
                //webFilter.filterByGet(serverWebExchange);
                return chain.filter(serverWebExchange.mutate().request(serverWebExchange.getRequest()).build());
            }
        }).filter((serverWebExchange, gatewayFilterChain) ->
                headerFilter(serverWebExchange, gatewayFilterChain)).circuitBreaker(configConsumer ->
                configConsumer.setFallbackUri("forward:/fallback").setName("fallbackCmd"));
    }

    /**
     * 文本内容过滤
     *
     * @return
     */
    public Function<GatewayFilterSpec, UriSpec> filterPost() {

        return filterSpec -> filterSpec.filter((serverWebExchange, gatewayFilterChain) -> {
            String body = GatewayHttpUtil.getBodyContent(serverWebExchange);

            final String uri = serverWebExchange.getRequest().getURI().getPath();
            final HttpMethod method = serverWebExchange.getRequest().getMethod();
            final HttpHeaders headers = serverWebExchange.getRequest().getHeaders();
            log.info("RECV:{} {} \r\n{} \r\n{}", method, uri, headers, body);

            /**
             * 请求头数据判断。
             * 根据不同的客户端进行在不同的头里面获取不同的数据值。
             *
             */
            if (!headers.containsKey(Authorization)) {
                // 如果没有头 直接进行URL检测，如果URL检擦是白名单直接进行放行。如果非白名单进行拦截。
                String url = serverWebExchange.getRequest().getURI().getPath();
                if (isAllow(url)) {
                    //todo 添加新请求体 和租户头 示例
                }
            } else {

                final String authorization = headers.get(Authorization).get(0);
            }
            return gatewayFilterChain.filter(serverWebExchange.mutate().request(serverWebExchange.getRequest()).build());
        }).filter((serverWebExchange, gatewayFilterChain) -> headerFilter(serverWebExchange, gatewayFilterChain))
                .circuitBreaker(configConsumer -> configConsumer.setFallbackUri("forward:/fallback").setName("fallbackCmd"));
    }




    /**
     * 文件上传接口数据校验
     *
     * @return
     */
    private Function<GatewayFilterSpec, UriSpec> fileUpload() {
        return filterSpec -> filterSpec.filter((serverWebExchange, chain) -> {
            String uri = serverWebExchange.getRequest().getURI().getPath();
            HttpMethod method = serverWebExchange.getRequest().getMethod();
            HttpHeaders headers = serverWebExchange.getRequest().getHeaders();
            log.info("RECV:file {} {} {} ", method, uri, headers);
            if (method != HttpMethod.POST) {
                log.warn("非post请求，uri={}", uri);
            }

            //判断请求头
            if (!headers.containsKey(Authorization)) {
                log.warn("{}，请求头{}不存在,{}", uri, Authorization, headers);
            }
            final String authorization = headers.get(Authorization).get(0);
            return chain.filter(serverWebExchange.mutate().request(serverWebExchange.getRequest()).build());
        }).filter((serverWebExchange, gatewayFilterChain) -> headerFilter(serverWebExchange, gatewayFilterChain)).circuitBreaker(configConsumer -> configConsumer.setFallbackUri("forward:/fallback").setName("fallbackCmd"));
    }


    /**
     * 用户信息
     */
    public static final String USER = "user";
    public static final String AUTH_HEADER = "auth_header";

    private Mono<Void> headerFilter(ServerWebExchange serverWebExchange, GatewayFilterChain gatewayFilterChain) {
        final String customer = (String) serverWebExchange.getAttributes().get(USER);
        final String authHeader = (String) serverWebExchange.getAttributes().get(AUTH_HEADER);
        if (StringUtils.hasText(customer) || StringUtils.hasText(authHeader)) {
            Consumer<HttpHeaders> httpHeaders = httpHeader -> {
                if (StringUtils.hasText(customer)) {
                    httpHeader.set(USER, customer);
                }
                if (!StringUtils.hasText(authHeader)) {
                    httpHeader.set(AUTH_HEADER, authHeader);
                }
            };
            ServerHttpRequest httpRequest = serverWebExchange.getRequest().mutate().headers(httpHeaders).build();
            ServerWebExchange build = serverWebExchange.mutate().request(httpRequest).build();
            return gatewayFilterChain.filter(build);
        }

        return gatewayFilterChain.filter(serverWebExchange);
    }

    /**
     * url 过滤
     */
    private boolean isAllow(String url) {
        return true;
    }
}