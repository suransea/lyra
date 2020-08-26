package com.sea.lyrad.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class Configuration {
    private static final int PORT;
    private static final int THREAD_NUMBER;

    static {
        Properties properties = new Properties();
        try {
            InputStream inputStream = Configuration.class.getResourceAsStream("lyrad.conf");
            properties.load(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        } catch (IOException e) {
            Log.pa("The configure file was lost.");
            System.exit(1);
        }
        PORT = Integer.parseInt(properties.getProperty("port"));
        THREAD_NUMBER = Integer.parseInt(properties.getProperty("n_threads"));
    }

    public static int getPort() {
        return PORT;
    }

    public static int getThreadNumber() {
        return THREAD_NUMBER;
    }
}
