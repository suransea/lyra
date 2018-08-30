package com.sea.pxx;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Main {

    public static void main(String[] args) {
        //解析命令行参数
        ArgsParser argsParser = new ArgsParser(args);

        String username;
        String passwd;
        String address;

        if (argsParser.getUsername() == null) {
            username = "root";
        }

        if (argsParser.getAddress() == null) {
            address = "127.0.0.1";
        } else {
            address = argsParser.getAddress();
        }

        //TODO: 用户登录
        Socket socket;
        try {
            socket = new Socket();
            InetSocketAddress inetSocketAddress = new InetSocketAddress(address, 5494);
            socket.connect(inetSocketAddress, 10000);
        } catch (UnknownHostException u) {
            Log.pa("错误的IP地址.");
        } catch (IOException ioe) {
            Log.pa("连接超时.");
        }
        Log.close();
    }
}
