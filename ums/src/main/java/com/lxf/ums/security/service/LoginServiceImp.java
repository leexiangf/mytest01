package com.lxf.ums.security.service;

import com.alibaba.fastjson.JSONObject;
import com.lxf.common.result.JsonResult;
import com.lxf.ums.pojo.entity.SysRole;
import com.lxf.ums.pojo.entity.User;
import com.lxf.ums.pojo.request.SmsLoginRequest;
import com.lxf.ums.pojo.request.UserLoginRequest;
import com.lxf.ums.repository.UserRepository;
import com.lxf.ums.security.jwt.JWTUtil;
import com.lxf.ums.security.sms.SmsCodeAuthenticationToken;
import com.lxf.ums.security.userDetail.LoginUser;
import com.lxf.ums.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @ClassName : LoginServiceImp
 * @Description
 * @Date 2022/3/27 16:04
 * @Created lxf
 */
@Slf4j
@Service
public class LoginServiceImp  {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 密码登录
     * @param request
     * @return
     */
    public JsonResult<HashMap> login(UserLoginRequest request){
        log.info("進來了！密码登录");
        //封装到这个类中
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());
        //通过这个类获取到对应的信息
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        //Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //认证不通过
        if(Objects.isNull(authentication)){
            new RuntimeException("登录失败");
        }

        HashMap<String, Object> map = buildUser(authentication);

        return   JsonResult.success(map);

    }

    /**
     * 短信登录
     * @param request
     * @return
     */
    public JsonResult smsLogin(SmsLoginRequest request){
        log.info("進來了！短信登录");
        SmsCodeAuthenticationToken authenticationToken
                = new SmsCodeAuthenticationToken(request.getPhone(), request.getCode());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        HashMap<String, Object> map = buildUser(authentication);
        return   JsonResult.success(map);
    }

    /**
     * 发送短信
     * @param phone
     * @return
     */
    public JsonResult sendSms(String phone){
        // 1. 获取到手机号
        log.info(phone + "请求获取验证码");
        // 2. 模拟调用短信平台获取验证码，以手机号为KEY，验证码为值，存入Redis，过期时间一分钟
        //String code = RandomStringUtils.random(6);
        String code = "666666";
        redisUtil.set(phone, code, 60*60);
        String saveCode = (String) redisUtil.get(phone);// 缓存中的code
        Long expire = redisUtil.getExpire(phone); // 查询过期时间
        // 3. 验证码应该通过短信发给用户，这里直接返回吧
        Map<String,String> result=new HashMap<>();
        result.put("code",saveCode);
        result.put("expire",expire+"秒");
        return JsonResult.success(result);
    }


    /**
     * 登出
     * @return
     */
    public JsonResult logout(){
        //获取securityContext中的值
        UsernamePasswordAuthenticationToken authentication =
                (UsernamePasswordAuthenticationToken)SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser)authentication.getPrincipal();
        //删除redis中的值
        redisUtil.del(loginUser.getUser().getUserId(),"authorities"+loginUser.getUser().getUserId());
         return   JsonResult.OK();
    }

    /**
     * 封装result，保存用户信息到redis
     * @param authentication
     * @return
     */
    private HashMap<String, Object> buildUser(Authentication authentication){
        //认证通过，生成一个token
        LoginUser principal = (LoginUser) authentication.getPrincipal();
        User user = principal.getUser();
        String userId = user.getUserId();
        String token = JWTUtil.createToken(userId);
        log.info(" token ---->>>>> {}",token);
        HashMap<String, Object> map = new HashMap<>();
        user.setPassword("");
        map.put("token",token);
        map.put("userInfo",user);
        //可以把user放入redis中，减少数据库压力
        redisUtil.set(userId,user,60*60);
        //log.info("redis userid ---->>>> {}",redisTemplate.opsForValue().get(userId).toString());

        List<SysRole> roles = userRepository.getRoleByUserId(userId);
        List<String> rolesNames = roles.stream().map((role) -> {
            return role.getRoleName();
        }).collect(Collectors.toList());
        redisUtil.del("authorities"+userId);
        String jsonString = JSONObject.toJSONString(rolesNames);
        redisUtil.set("authorities"+userId,jsonString,60*60);

        return map;
    }
}
