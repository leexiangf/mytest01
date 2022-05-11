package com.lxf.ums.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.lxf.common.enums.ResultCode;
import com.lxf.common.result.JsonResult;
import com.lxf.ums.mapper.UserMapper;
import com.lxf.ums.pojo.entity.User;
import com.lxf.ums.service.WxService;
import com.lxf.ums.utils.AuthUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

/**
 * @ClassName : WxServiceImpl
 * @Description TODO
 * @Date 2022/4/17 13:01
 * @Created lxf
 */
@Service
@Slf4j
public class WxServiceImpl implements WxService {

    private final String wxAuthCallBackUrl="https://api.weixin.qq.com/sns/oauth2/access_token?appid=";
    private final String wxUserInfoCallBackUrl="https://api.weixin.qq.com/sns/userinfo?access_token=";

    @Autowired
    private UserMapper userMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonResult callBack(String code) {
        System.out.println("code:" + code);
        //获取code后，请求以下链接获取access_token
        StringBuilder builder = new StringBuilder(wxAuthCallBackUrl);
        builder.append(AuthUtil.APPID);
        builder.append("&secret=");
        builder.append(AuthUtil.APPSECRET);
        builder.append("&code=");
        builder.append(code);
        builder.append("&grant_type=authorization_code");

        //通过网络请求方法来请求上面这个接口
        //将StringBuilder转换成String
        String url=builder.toString();
        JSONObject jsonObject = null;
        try {
            jsonObject = AuthUtil.doGetJson(url);
        } catch (IOException e) {
            e.printStackTrace();
            return JsonResult.error(ResultCode.EXCEPTION.code(),"操作失败");
        }

        System.out.println("==========================jsonObject" + jsonObject);
        //从返回的JSON数据中取出access_token和openid，拉取用户信息时用
        String token = jsonObject.getString("access_token");
        String openid = jsonObject.getString("openid");

        // 第三步：刷新access_token（如果需要）

        // 第四步：拉取用户信息(需scope为 snsapi_userinfo)
        StringBuilder builder1 = new StringBuilder(wxUserInfoCallBackUrl);
        builder1.append(token);
        builder1.append("&openid=");
        builder1.append(openid);
        builder1.append("&lang=zh_CN");
        //通过网络请求方法来请求上面这个接口
        //将StringBuilder转换成String
        String infoUrl=builder1.toString();
        JSONObject userInfo = null;
        try {
            userInfo = AuthUtil.doGetJson(infoUrl);
        } catch (IOException e) {
            e.printStackTrace();
            return JsonResult.error(ResultCode.EXCEPTION.code(),"操作失败");
        }

        System.out.println("userInfo=======" + userInfo);

        //第1种情况：使用微信用户信息直接登录，无需注册和绑定
//        model.addAttribute("info",userInfo);
//        return "wx/wxtest1";
//

        //第2种情况：查看数据库是否存在相对应用户
            User wxuser = new User();
            wxuser =userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getOpenid,openid));
            System.out.println("wxuser=======" + wxuser);
            if (wxuser != null) {
                //已绑定
                return JsonResult.OK();
            } else {
                //未绑定
                User user = userInfo.toJavaObject(User.class);
                userMapper.insert(user);
                user.setOpenid(openid);
                return JsonResult.success(user);
            }
    }



}
