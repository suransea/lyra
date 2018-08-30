package com.sea.pxxd;

import java.io.FileReader;
import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;

public class PxxServer {
    public static void main(String[] args) {
        Properties properties = new Properties();
        try {
            properties.load(new FileReader("pxxd.conf"));
        } catch (IOException e) {
            Log.pa("The configure file was lost.");
            System.exit(1);
        }
        int port = Integer.parseInt(properties.getProperty("port"));

        try {
            ServerSocket serverSocket = new ServerSocket(port);
            while (true) {
                Socket socket = serverSocket.accept();
                Thread thread = new Thread(new PxxServerThread(socket));
                thread.start();
            }
        } catch (BindException b) {
            Log.pa("端口已被占用.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
