package com.lxf.ums.security.filter;


import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.lxf.ums.pojo.entity.SysRole;
import com.lxf.ums.pojo.entity.User;
import com.lxf.ums.repository.UserRepository;
import com.lxf.ums.security.jwt.JWTUtil;
import com.lxf.ums.security.userDetail.LoginUser;
import com.lxf.ums.utils.RedisUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @ClassName : JwtAuthenticationTokenFilter
 * @Description jwt 过滤器
 * @Date 2022/3/27 16:35
 * @Created lxf
 */
@Slf4j
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {


    @Autowired
    UserRepository userRepository;
    @Autowired
    private RedisUtil redisUtil;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //获取token
        String token = request.getHeader("base_token");
        log.info(" token  <<<<<--------: "+token);
        if(StringUtils.isEmpty(token)){
            //如果token为空，放行，后面再拦截
            filterChain.doFilter(request,response);
            return;
        }
        //解析token
        Claims claims = JWTUtil.parseToken(token);
        String userId = claims.getSubject();
        log.info("userid <<<<------- :{}",userId);
        //从数据库获取到user信息
        log.info("redis <<<<------ : {}",redisUtil.get(userId));
        User user = (User)redisUtil.get(userId);
        if(Objects.isNull(user)){
            throw  new UsernameNotFoundException("用户未登录");
        }
        //获取用户权限角色信息
        String o = (String) redisUtil.get("authorities" + userId);
        List<String> range = JSONObject.parseArray(o, String.class);
        log.info("redis role list size: {}",range.size());
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        //如果redis中有，就从redis中获取
        if(Objects.nonNull(range)&&range.size()>0){
            authorities =
                    range.stream()
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList());
        }else {
            // 如果Redis中没有，就在数据库中查询，并放入redis
            List<SysRole> roles = userRepository.getRoleByUserId(userId);
            List<String> rolesNames = roles.stream().map((role) -> {
                return role.getRoleName();
            }).collect(Collectors.toList());
            authorities =
                    rolesNames.stream()
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList());
            redisUtil.set
                    ("authorities"+userId,JSONObject.toJSONString(rolesNames),3600l);
        }
        //存入securityContextHolder
        //封装到这个类中     获取权限信息user.getAuthorities()
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(user,null,authorities);

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        //放行
        filterChain.doFilter(request,response);

    }
}
