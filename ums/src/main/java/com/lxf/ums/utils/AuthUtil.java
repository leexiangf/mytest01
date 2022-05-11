package com.lxf.ums.utils;

import com.alibaba.druid.util.HttpClientUtils;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * @ClassName : AuthUtil
 * @Description TODO
 * @Date 2022/4/16 23:20
 * @Created lxf
 */
public class AuthUtil {
    public static final String APPID = "";
    public static final String APPSECRET = "";

    //回调地址
    public static final String backUrl="";//如果你没有在application.yml文件中设置 context-path: /api，那么api就去掉
    //    public static final String backUrl="http://localhost:8080/callBack";
    public static JSONObject doGetJson(String url) throws ClientProtocolException, IOException {
        JSONObject jsonObject = null;
        // 创建HttpClient实例

        HttpClient client =  HttpClientBuilder.create().build();
        // 根据URL创建HttpGet实例
        HttpGet get = new HttpGet(url);
        // 执行get请求，得到返回体
        HttpResponse response = client.execute(get);
        System.out.println(response);
        //从response里面拿自己想要的结果
        HttpEntity entity = response.getEntity();
        if(entity != null){
            String result = EntityUtils.toString((HttpEntity) entity,"UTF-8");
            jsonObject = jsonObject.getJSONObject(result);
        }
        //把链接释放掉
//        HttpGet.releaseConnection();
        return jsonObject;
    }
}

