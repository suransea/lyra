package com.sea.lyrad.server;

import com.sea.lyrad.util.Log;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;

public class LyraServer {
    public static final String VERSION = "0.5"; //版本号

    public static void main(String[] args) {
        Properties properties = new Properties();
        try {
            InputStream inputStream = new FileInputStream("etc/lyrad.conf");
            properties.load(new InputStreamReader(inputStream, "utf-8"));
        } catch (IOException e) {
            Log.pa("The configure file was lost.");
            System.exit(1);
        }
        int port = Integer.parseInt(properties.getProperty("port"));

        int count = 0;//连接数

        try {
            ServerSocket serverSocket = new ServerSocket(port);
            Log.pa("The server is running.");
            //noinspection InfiniteLoopStatement
            while (true) {
                Socket socket = serverSocket.accept();
                count++;
                Log.p();
                Log.pa("Connection " + count);
                Log.pa(socket);
                Log.p();
                Thread thread = new Thread(new LyraServerThread(socket, count));
                thread.start();
            }
        } catch (BindException e) {
            Log.pa("端口已被占用.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
