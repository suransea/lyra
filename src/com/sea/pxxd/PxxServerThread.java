package com.sea.pxxd;

import java.net.Socket;

public class PxxServerThread implements Runnable {

    Socket socket;

    public PxxServerThread(Socket s) {
        socket = s;
    }

    @Override
    public void run() {
        Log.pa("connected.");
    }
}
