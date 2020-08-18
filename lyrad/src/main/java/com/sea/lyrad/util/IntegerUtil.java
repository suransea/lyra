package com.sea.lyrad.util;

public class IntegerUtil {
    /**
     * 将int值转换成字节数组，高位在前
     *
     * @param integer 目标整数
     * @return 转换后的字节数组
     */
    public static byte[] toByteArray(int integer) {
        byte[] bytes = new byte[4];
        bytes[0] = (byte) ((integer >> 24) & 0xff);
        bytes[1] = (byte) ((integer >> 16) & 0xff);
        bytes[2] = (byte) ((integer >> 8) & 0xff);
        bytes[3] = (byte) (integer & 0xff);
        return bytes;
    }
}
