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
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.concurrent.locks.Lock;

public class LyraServerThread implements Runnable {

    private static final String VERSION = "0.5";//版本号

    private Lock lock;

    private Socket socket;
    private int count;
    private InputStream inputStream;
    private OutputStream outputStream;
    private Scanner scanner;
    private User user = null;
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
                String request = scanner.nextLine();
                lock.lock();
                parse(request);
                lock.unlock();
            }
        } catch (IOException | NoSuchElementException ioe) {
            Log.pa("连接 " + count + " 已断开.");
        } finally {
            scanner.close();
            try {
                lock.unlock();
                inputStream.close();
                outputStream.close();
                socket.close();
            } catch (Exception e) {
                Log.a(e);
            }

        }
    }

    private void parse(String request) throws IOException {
        JSONObject json = new JSONObject(request);
        switch (json.getString("tag")) {
            case "login": {
                String username = json.getString("user");
                String password = json.getString("password");
                DBManager dbManager = DBManager.getInstance();
                boolean access;
                try {
                    access = dbManager.verify(username, password);
                } catch (DBProcessException e) {
                    access = false;
                }
                JSONObject response = new JSONObject();
                response.put("access", access);
                if (access) {
                    response.put("version", VERSION);
                    response.put("count", count);
                    user = new User(username);
                }
                send(response.toString());
                break;
            }
            case "sql": {
                if (user == null) {
                    return;
                }
                String sql = json.getString("sql");
                Lexer lexer = new Lexer(sql);
                SQLParser parser = new SQLParser(lexer);
                JSONObject response = new JSONObject();
                try {
                    long startTime = System.currentTimeMillis();
                    SQLStatement statement = parser.parse();
                    String outcome = sqlExecutor.execute(user, statement);
                    long time = System.currentTimeMillis() - startTime;
                    response.put("outcome", outcome);
                    response.put("time", time);
                    response.put("complete", true);
                } catch (DBProcessException | SQLParseException |
                        SQLParseUnsupportedException | UnterminatedCharException e) {
                    response.put("outcome", e.getMessage());
                    response.put("complete", false);
                }
                outputStream.write(toByteArray(response.toString().getBytes("utf-8").length));
                outputStream.flush();
                send(response.toString());
                break;
            }
        }
    }

    private void send(String response) throws IOException {
        outputStream.write(response.getBytes("utf-8"));
        outputStream.flush();
    }

    private byte[] toByteArray(int integer) {
        byte[] bytes = new byte[4];
        bytes[0] = (byte) ((integer >> 24) & 0xff);
        bytes[1] = (byte) ((integer >> 16) & 0xff);
        bytes[2] = (byte) ((integer >> 8) & 0xff);
        bytes[3] = (byte) (integer & 0xff);
        return bytes;
    }
}
