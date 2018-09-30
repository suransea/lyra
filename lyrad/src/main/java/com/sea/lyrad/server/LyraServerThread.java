package com.sea.lyrad.server;

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

    private Lock lock;
    private Socket socket;
    private int count; //线程ID
    private InputStream inputStream;
    private OutputStream outputStream;
    private Scanner scanner;
    private LyraHandler handler;

    public LyraServerThread(Socket socket, int count) {
        this.socket = socket;
        this.count = count;
        lock = LockUtil.getSingleLock();
        try {
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
            scanner = new Scanner(inputStream, "utf-8");
            handler = new LyraHandler(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
            Log.a("ERROR: 流获取失败.");
        }
    }

    @Override
    public void run() {
        boolean locked = false;
        try {
            //noinspection InfiniteLoopStatement
            while (true) {
                String request = scanner.nextLine();
                lock.lock();
                locked = true;
                handle(request); //处理请求
                lock.unlock();
                locked = false;
            }
        } catch (IOException | NoSuchElementException e) {
            Log.pa("连接 " + count + " 已断开.");
        } finally {
            scanner.close();
            try {
                if (locked) lock.unlock();
                inputStream.close();
                outputStream.close();
                socket.close();
            } catch (Exception e) {
                Log.a(e);
            }
        }
    }

    private void handle(String request) throws IOException {
        JSONObject json = new JSONObject(request);
        switch (json.getString("tag")) {
            case "login": {
                handler.handleLogin(json, count);
                break;
            }
            case "sql": {
                handler.handleSQL(json); //处理普通SQL执行
                break;
            }
            case "pre": {
                handler.handlePrepare(json);
                break;
            }
            case "exec": {
                handler.handleExecute(json); //处理prepared SQL的执行
                break;
            }
            case "close": {
                handler.handleClose(json);
                break;
            }
        }
    }
}
