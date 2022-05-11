package com.lxf.eas.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @ClassName : DateUtil
 * @Description
 * @Date 2022/4/26 21:39
 * @Created lxf
 */
public class DateUtil {

    /**
     SimpleDateFormat函数语法：

     G 年代标志符
     y 年
     M 月
     d 日
     h 时 在上午或下午 (1~12)
     H 时 在一天中 (0~23)
     m 分
     s 秒
     S 毫秒
     E 星期几
     D 一年中的第几天
     F 一月中第几个星期 /7
     w 一年中第几个星期
     W 一月中第几个星期 实际
     a 上午 / 下午 标记符
     k 时 在一天中 (1~24)
     K 时 在上午或下午 (0~11)
     z 时区
     */

    public static void main(String[] args) {
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat( "一年中的第 D 天 一年中第w个星期 一月中第W个星期 在一天中k时 z时区");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy:MM:dd-HH:mm:ss");
        SimpleDateFormat sdf2 = new SimpleDateFormat("E");
        String format = sdf.format(now);
        String format1 = sdf1.format(now);
        String format2 = sdf2.format(now);
        System.out.println(format);
        System.out.println(format1);
        System.out.println(format2);
        System.out.println("-------------------------------------------");
        Calendar calendar = Calendar.getInstance();
        System.out.println(calendar.get(Calendar.WEDNESDAY));
        System.out.println(calendar.get(Calendar.YEAR));
        System.out.println(calendar.get(Calendar.MONTH)+1);
        System.out.println(calendar.get(Calendar.WEEK_OF_YEAR));
        calendar.setTime(new Date());
    }
}
