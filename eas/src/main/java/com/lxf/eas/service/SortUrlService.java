//package com.lxf.eas.service;
//
//import com.lxf.eas.utils.HexUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import javax.servlet.http.HttpServletRequest;
//import java.io.UnsupportedEncodingException;
//import java.util.Base64;
//
///**
// * @ClassName : SortUrlService
// * @Description  短连接功能实现
// * @Date 2022/4/25 20:40
// * @Created lxf
// */
//@Service
//public class SortUrlService {
//
//    @Autowired
//    private RedisTemplate redisTemplate;
//
//    private String redisCountKey="sort:count";
//    private String redisMapKey="sort:map";
//
//
//    /**
//     *  长链接通过算法变短连接、并保存在redis中
//     * @param url
//     * @return
//     * @throws UnsupportedEncodingException
//     */
//    public String sortUrl(HttpServletRequest request,String url) throws UnsupportedEncodingException {
//        String requestUrl = request.getRequestURL().toString();
//        redisTemplate.opsForValue().setIfAbsent(redisCountKey,100000);
//        Long count = redisTemplate.opsForValue().increment(redisCountKey);
//        byte[] encode = Base64.getUrlEncoder().encode(HexUtils.removeLeftZero(HexUtils.long2bytes(count)));
//        String sortUrl = new String(encode, "UTF-8");
//
//        redisTemplate.opsForHash().put(redisMapKey,sortUrl,url);
//        return sortUrl;
//    }
//
//
//    /**
//     * 通过短连接获取长链接
//     * @param sortUrl
//     * @return
//     */
//    public String sendRedirect(String sortUrl){
//        String url = (String) redisTemplate.opsForHash().get(redisMapKey, sortUrl);
//        return url;
//    }
//}
