package com.sea.lyrad.server;

import com.sea.lyrad.exec.DBManager;
import com.sea.lyrad.exec.DBProcessException;
import com.sea.lyrad.exec.SQLExecutor;
import com.sea.lyrad.exec.User;
import com.sea.lyrad.lex.Lexer;
import com.sea.lyrad.lex.analyze.UnterminatedCharException;
import com.sea.lyrad.parse.SQLParseException;
import com.sea.lyrad.parse.SQLParseUnsupportedException;
import com.sea.lyrad.parse.SQLParser;
import com.sea.lyrad.parse.stmt.SQLStatement;
import com.sea.lyrad.util.LockUtil;
import com.sea.lyrad.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.concurrent.locks.Lock;

public class LyraServerThread implements Runnable {

    private static final String VERSION = "0.4";//版本号

    private Lock lock;

    private Socket socket;
    private int count;
    private InputStream inputStream;
    private OutputStream outputStream;
    private Scanner scanner;
    private User user;
    private SQLExecutor sqlExecutor;

    public LyraServerThread(Socket socket, int count) {
        this.socket = socket;
        this.count = count;
        sqlExecutor = new SQLExecutor();
        lock = LockUtil.getSingleLock();
        try {
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
            scanner = new Scanner(inputStream, "utf-8");
        } catch (IOException ioe) {
            ioe.printStackTrace();
            Log.a("ERROR: 流获取失败.");
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                lock.lock();
                String str = scanner.nextLine();
                parse(str);
                lock.unlock();
            }
        } catch (IOException | NoSuchElementException ioe) {
            Log.pa("连接 " + count + " 已断开.");
        } finally {
            lock.unlock();
            scanner.close();
            try {
                inputStream.close();
                outputStream.close();
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private void parse(String s) throws IOException {
        Scanner strScanner = new Scanner(s);
        switch (strScanner.next()) {
            case "login": {
                String username = strScanner.next();
                String password = strScanner.next();
                DBManager dbManager = new DBManager();
                boolean access;
                try {
                    access = dbManager.verify(username, password);
                } catch (DBProcessException e) {
                    access = false;
                }

                if (access) {
                    send(String.format("access %s %d\n", VERSION, count));
                    user = new User(username);
                } else {
                    send("refuse\n");
                }
                outputStream.flush();
                break;
            }
            case "sql": {
                String sql = scanner.nextLine();
                Lexer lexer = new Lexer(sql);
                SQLParser parser = new SQLParser(lexer);
                try {
                    long startTime = System.currentTimeMillis();
                    SQLStatement statement = parser.parse();
                    String outcome = sqlExecutor.execute(user, statement);
                    long time = System.currentTimeMillis() - startTime;
                    send(outcome);
                    send(String.format("\n\nConsumption of time: %.3f s.", time / 1000.0));
                    send("\ntrue\n");
                    outputStream.flush();
                } catch (DBProcessException | SQLParseException |
                        SQLParseUnsupportedException | UnterminatedCharException e) {
                    send(e.getMessage());
                    send("\nfalse\n");
                }
                break;
            }
        }
    }

    private void send(String response) throws IOException {
        outputStream.write(response.getBytes("utf-8"));
        outputStream.flush();
    }
}
