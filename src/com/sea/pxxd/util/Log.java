package com.sea.pxxd.util;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Log {
    private static Writer writer = null;

    static {
        try {
            writer = new FileWriter("pxxd.log", true);
        } catch (IOException e) {
            Log.p("Warning: 日志初始化失败.");
        }
    }

    /**
     * 向控制台打印
     */
    public static void p(Object o) {
        StringBuilder log = new StringBuilder();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置时间格式
        log.append(simpleDateFormat.format(new Date()));
        log.append(' ');
        log.append(o.toString());
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

    public static void close() {
        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
