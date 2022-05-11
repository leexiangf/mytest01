package com.lxf.eas.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName : HexUtils
 * @Description
 * @Date 2022/4/25 20:20
 * @Created lxf
 */
public class HexUtils {
    /**
     *  long数字转成8字节byte数组
     * @param num
     * @return
     */
    public static byte[] long2bytes(long num) {
        byte[] bytes = new byte[8];
        for (int ix = 0; ix < Long.BYTES; ++ix) {
            int offset = Long.SIZE - (ix + 1) * Byte.SIZE;
            bytes[ix] = (byte) ((num >> offset) & 0xff);
        }
        return bytes;
    }

    /**
     * 去除左边为0的字节
     * @param bytes
     * @return
     */
    public static byte[] removeLeftZero(byte[] bytes) {
        List<Byte> list = new ArrayList();
        boolean filter = true;
        for (int i = 0; i < bytes.length; i++) {
            if (filter) {
                if (bytes[i] == 0) {
                    continue;
                } else {
                    list.add(bytes[i]);
                    filter = false;
                }
            } else {
                list.add(bytes[i]);
            }
        }
        byte[] result = new byte[list.size()];
        for (int i = 0; i < list.size(); i++) {
            result[i] = list.get(i);
        }
        return result;
    }
}
