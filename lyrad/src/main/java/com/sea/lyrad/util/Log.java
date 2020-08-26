package com.sea.lyrad.util;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日至工具
 */
public class Log {
    private static Writer writer;

    static {
        try {
            OutputStream outputStream = new FileOutputStream("/log/lyrad.log", true);
            writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
        } catch (IOException e) {
            Log.p("Warning: 日志初始化失败.");
        }
    }

    private Log() {
    }

    /**
     * 向控制台打印
     */
    public static void p(Object o) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置时间格式
        String log = format.format(new Date()) + ' ' + o.toString();
        System.out.println(log);
    }

    public static void p() {
        System.out.println();
    }

    /**
     * 添加到日志文件中
     */
    public static void a(Object o) {
        StringBuilder log = new StringBuilder();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置时间格式
        log.append(simpleDateFormat.format(new Date()));
        log.append(' ');
        log.append(o.toString());
        try {
            writer.append(log.toString());
            writer.append('\n');
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void pa(Object o) {
        p(o);
        a(o);
    }
}
