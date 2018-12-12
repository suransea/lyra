package com.sea.lyrad.server;

import com.sea.lyrad.util.Log;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;

public class ReadHandler implements CompletionHandler<Integer, ByteBuffer> {
    private LyraHandler handler;
    private AsynchronousSocketChannel channel;

    public ReadHandler(AsynchronousSocketChannel channel) {
        this.channel = channel;
        handler = new LyraHandler(channel);
    }

    @Override
    public void completed(Integer integer, ByteBuffer buffer) {
        if (integer == -1) {
            Log.pa(String.format("Connection %d was interrupted.", AcceptHandler.getConnectionId()));
            try {
                channel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        String request = new String(buffer.array(), 0, buffer.position(), StandardCharsets.UTF_8);
        handler.handle(request);
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
