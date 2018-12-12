package com.sea.lyrad.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

public class ConfigureUtil {
    private static int port;
    private static int threadNumber;

    static {
        Properties properties = new Properties();
        try {
            InputStream inputStream = new FileInputStream("etc/lyrad.conf");
            properties.load(new InputStreamReader(inputStream, "utf-8"));
        } catch (IOException e) {
            Log.pa("The configure file was lost.");
            System.exit(1);
        }
        port = Integer.parseInt(properties.getProperty("port"));
        threadNumber = Integer.parseInt(properties.getProperty("n_threads"));
    }

    public static int getPort() {
        return port;
    }

    public static int getThreadNumber() {
        return threadNumber;
    }
}
