package com.sea.lyrad.server;

import com.sea.lyrad.util.Log;

import java.io.FileReader;
import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;

public class LyraServer {
    public static void main(String[] args) {
        Properties properties = new Properties();
        try {
            properties.load(new FileReader("etc/lyrad.conf"));
        } catch (IOException e) {
            Log.pa("The configure file was lost.");
            System.exit(1);
        }
        int port = Integer.parseInt(properties.getProperty("port"));

        int count = 0;//连接数

        try {
            ServerSocket serverSocket = new ServerSocket(port);
            Log.pa("The server is running.");
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
        } catch (BindException b) {
            Log.pa("端口已被占用.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
