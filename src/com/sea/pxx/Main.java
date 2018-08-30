package com.sea.pxx;

import java.io.Console;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

public class Main {

    public static void main(String[] args) {
        //解析命令行参数
        ArgsParser argsParser = new ArgsParser(args);

        String username = argsParser.getUsername();
        String passwd;
        String address = argsParser.getAddress();
        if (argsParser.isPassword()) {
            System.out.print("password: ");
            Console console = System.console();
            passwd = new String(console.readPassword());
        } else {
            passwd = "";
        }

        //创建socket连接数据库服务器
        Socket socket;
        try {
            socket = new Socket();
            InetSocketAddress inetSocketAddress = new InetSocketAddress(address, 5494);
            socket.connect(inetSocketAddress, 10000);
        } catch (UnknownHostException u) {
            Log.pa("错误的IP地址.");
        } catch (SocketTimeoutException ste) {
            Log.pa("连接超时.");
        } catch (IOException ioe) {
            Log.pa("服务器未响应.");
        }



        Log.close();
    }
}
