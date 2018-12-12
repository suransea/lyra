package com.sea.lyrad.server;

import com.sea.lyrad.util.Log;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class AcceptHandler implements CompletionHandler<AsynchronousSocketChannel, Void> {
    private static final int BUFFER_SIZE = 1048576;
    private static int connectionId = 0;
    private AsynchronousServerSocketChannel serverSocketChannel;

    public AcceptHandler(AsynchronousServerSocketChannel channel) {
        serverSocketChannel = channel;
    }

    public static int getConnectionId() {
        return connectionId;
    }

    @Override
    public void completed(AsynchronousSocketChannel channel, Void aVoid) {
        connectionId++;
        Log.p();
        Log.pa("Connection " + connectionId);
        Log.pa(channel);
        Log.p();
        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
        ReadHandler handler = new ReadHandler(channel);
        channel.read(buffer, buffer, handler);
        serverSocketChannel.accept(null, new AcceptHandler(serverSocketChannel));
    }

    @Override
    public void failed(Throwable throwable, Void aVoid) {
        throwable.printStackTrace();
        Log.pa("A connection request received, but failed to connect.");
    }
}
