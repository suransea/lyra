package com.sea.pxx;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Properties;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        //解析命令行参数
        ArgsParser argsParser = new ArgsParser(args);

        String username = argsParser.getUsername();
        String passwd = "(none)";
        String address = argsParser.getAddress();
        if (argsParser.isPassword()) {
            System.out.print("password: ");
            Console console = System.console();
            passwd = new String(console.readPassword());
        }

        //解析配置文件
        Properties properties = new Properties();
        try {
            properties.load(new FileReader("../pxx.conf"));
        } catch (IOException e) {
            Log.pa("The configure file was lost.");
            System.exit(1);
        }

        //创建socket连接数据库服务器
        Socket socket = null;
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            socket = new Socket();
            InetSocketAddress inetSocketAddress = new InetSocketAddress(address, 5494);
            socket.connect(inetSocketAddress, 10000);
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
        } catch (UnknownHostException u) {
            Log.pa("错误的IP地址.");
            System.exit(2);
        } catch (SocketTimeoutException ste) {
            Log.pa("连接超时.");
            System.exit(3);
        } catch (IOException ioe) {
            Log.pa("服务器未响应.");
            System.exit(4);
        }

        //登录
        String login = "login " + username + ' ' + passwd + '\n';
        try {
            outputStream.write(login.getBytes());
            outputStream.flush();
            while (inputStream.available() == 0) ;
        } catch (IOException ioe) {
            Log.pa("服务器未响应.");
            System.exit(4);
        }
        Scanner scanner = new Scanner(inputStream);

        String version = "";
        int count = 0;
        switch (scanner.next()) {
            case "access": {
                version = scanner.next();
                count = scanner.nextByte();
                break;
            }
            case "refuse": {
                Log.pa("认证失败， 用户名或密码不正确.");
                System.exit(5);
            }
        }

        //打印提示信息
        StringBuilder tips = new StringBuilder();
        tips.append("\nWelcome to the pxx monitor.  Commands end with ; .\n");
        tips.append("Your connection id is ");
        tips.append(count);
        tips.append('\n');
        tips.append("Server version:");
        tips.append(version);
        tips.append("\n\n");
        tips.append("Type 'help;' or '\\h' for help. Type '\\c' to clear the current input statement.\n\n");
        System.out.print(tips);

        //处理输入
        Scanner screenScanner = new Scanner(System.in);
        String selectedDB = "(none)";
        System.out.print("pxx [" + selectedDB + "] > ");
        StringBuilder sql = new StringBuilder();
        int line = 0;
        while (true) {
            String cmd = screenScanner.nextLine();
            line++;
            if ((cmd.equals("quit") || cmd.equals("exit")) && line == 1) {
                Log.p("Bye.");
                try {
                    scanner.close();
                    screenScanner.close();
                    inputStream.close();
                    outputStream.close();
                    socket.close();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
                System.exit(0);
            }
            if (cmd.equals("\\c")) {
                sql = new StringBuilder();
                System.out.print("pxx [" + selectedDB + "] > ");
                line = 0;
                continue;
            }
            if (cmd.endsWith(";")) {
                sql.append(cmd);
                try {
                    outputStream.write("sql\n".getBytes());
                    outputStream.flush();
                    outputStream.write(sql.toString().getBytes());
                    outputStream.write("\n".getBytes());
                    outputStream.flush();
                    boolean finished;
                    Log.p();
                    while (true) {
                        String lineData = scanner.nextLine();
                        if (lineData.equals("true") || lineData.equals("false")) {
                            finished = Boolean.parseBoolean(lineData);
                            break;
                        }
                        Log.p(lineData);
                    }
                    Scanner sqlScanner = new Scanner(sql.toString());
                    if (sqlScanner.next().equals("use") && finished) {
                        sqlScanner.useDelimiter(";");
                        selectedDB = sqlScanner.next().trim();
                    }
                    Log.p();
                    sql = new StringBuilder();
                    System.out.print("pxx [" + selectedDB + "] > ");
                    line = 0;
                } catch (IOException ioe) {
                    Log.pa("服务器未响应.");
                    System.exit(4);
                }

            } else {
                sql.append(cmd);
                sql.append(' ');
                System.out.print("    -> ");
            }
        }
    }
}
