package com.lxf.ums.utils;

import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName : webUtil
 * @Description
 * @Date 2022/3/27 18:34
 * @Created lxf
 */
public class WebUtil {

    public static String renderString (HttpServletResponse response,String str){
        try {
            response.setStatus(200);
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.getWriter().print(str);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
