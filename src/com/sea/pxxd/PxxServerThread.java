package com.sea.pxxd;

import com.sea.pxxd.stmt.Statement;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class PxxServerThread implements Runnable {

    private static final String VERSION = "0.1";//版本号

    private Socket socket;
    private int count;
    private InputStream inputStream;
    private OutputStream outputStream;
    private SQLParser sqlParser;
    private DBManager dbManager;
    private Scanner scanner;

    public PxxServerThread(Socket s, int c) {
        sqlParser = new SQLParser();
        socket = s;
        count = c;
        try {
            inputStream = s.getInputStream();
            outputStream = s.getOutputStream();
            scanner = new Scanner(inputStream);
        } catch (IOException ioe) {
            ioe.printStackTrace();
            Log.a("ERROR: 流获取失败.");
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                String str = scanner.nextLine();
                parse(str);
            }
        } catch (IOException | NoSuchElementException ioe) {
            Log.pa("连接 " + count + " 似乎已断开.");
        }
    }

    private void parse(String s) throws IOException {
        Scanner strScanner = new Scanner(s);
        switch (strScanner.next()) {
            case "login": {
                String user = strScanner.next();
                String passwd = strScanner.next();
                boolean access = false;

                //TODO: 验证用户名和密码
                access = true;
                if (access) {
                    outputStream.write(String.format("access %s %d\n", VERSION, count).getBytes());
                } else {
                    outputStream.write("refuse\n".getBytes());
                }
                outputStream.flush();
                break;
            }
            case "sql": {
                String sql = scanner.nextLine();
                try {
                    Statement statement = sqlParser.parse(sql);
                    String outcome = statement.execute(dbManager);
                    outputStream.write(outcome.getBytes());
                    outputStream.write("\ntrue\n".getBytes());
                    outputStream.flush();
                } catch (DBProcessException | SQLParseException e) {
                    outputStream.write(e.getMessage().getBytes());
                    outputStream.write("\nfalse\n".getBytes());
                    outputStream.flush();
                }
                break;
            }
        }
    }
}
