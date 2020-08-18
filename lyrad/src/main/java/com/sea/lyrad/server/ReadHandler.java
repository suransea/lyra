package com.sea.lyrad.server;

import com.sea.lyrad.util.LockUtil;
import com.sea.lyrad.util.Log;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.locks.Lock;

public class ReadHandler implements CompletionHandler<Integer, ByteBuffer> {
    private LyraHandler handler;
    private AsynchronousSocketChannel channel;
    private int connectionId;
    private Lock lock;

    public ReadHandler(AsynchronousSocketChannel channel, int connectionId) {
        this.channel = channel;
        this.connectionId = connectionId;
        handler = new LyraHandler(channel, connectionId);
        lock = LockUtil.getSingleLock();
    }

    @Override
    public void completed(Integer integer, ByteBuffer buffer) {
        if (integer == -1) {
            Log.pa(String.format("Connection %d was interrupted.", connectionId));
            try {
                channel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        String request = new String(buffer.array(), 0, buffer.position(), StandardCharsets.UTF_8);
        lock.lock();
        handler.handle(request);
        lock.unlock();
        buffer.flip();
        buffer.clear();
        channel.read(buffer, buffer, this);
    }

    @Override
    public void failed(Throwable throwable, ByteBuffer buffer) {
        throwable.printStackTrace();
        Log.pa("A request read failed.");
    }
}
