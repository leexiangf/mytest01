package com.lxf.ums.controller;

import com.lxf.common.result.JsonResult;
import com.lxf.ums.service.WxService;
import com.lxf.ums.utils.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;

/**
 * @ClassName : WxAuthorizeController
 * @Description  微信授权登录
 * @Date 2022/4/16 23:35
 * @Created lxf
 */
@RestController
@RequestMapping("/weixin")
public class WxAuthorizeController {

    @Autowired
    private WxService wxService;


    /**
     * Tea微信登录接口
     * @throws IOException
     */
    //@IgnoreAuth
    @RequestMapping("/health-service/wxlogin")
    public void wx(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //第一步：引导用户进入授权页面同意授权，获取code
        StringBuilder builder = new StringBuilder("https://open.weixin.qq.com/connect/oauth2/authorize?appid=");
        builder.append(AuthUtil.APPID);
        builder.append("&redirect_uri=");
        builder.append(URLEncoder.encode(AuthUtil.backUrl));//开发文档要求转换
        builder.append("&response_type=code");
        builder.append("&scope=snsapi_userinfo");
        builder.append("&state=STATE#wechat_redirect");
        //授权页面地址
        String url=builder.toString();
        //重定向到授权页面
        response.sendRedirect(url);
    }


    /**
     * Tea微信登录接口回调
     */
    @RequestMapping(value="/callBack")
    public JsonResult wxcallback(@RequestParam("code") String code)throws IOException {
      return   wxService.callBack(code);
    }

}
