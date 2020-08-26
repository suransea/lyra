package com.sea.lyrad.server;

import com.sea.lyrad.util.Configuration;
import com.sea.lyrad.util.Log;

import java.net.BindException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LyraServer {
    public static final String VERSION = "0.5"; //版本号

    public static void main(String[] args) {
        int port = Configuration.getPort();
        int nThreads = Configuration.getThreadNumber();
        try {
            ExecutorService executor = Executors.newFixedThreadPool(nThreads);
            AsynchronousChannelGroup channelGroup = AsynchronousChannelGroup.withThreadPool(executor);
            AsynchronousServerSocketChannel channel = AsynchronousServerSocketChannel.open(channelGroup);
            channel.bind(new InetSocketAddress(port));
            CompletionHandler<AsynchronousSocketChannel, Void> acceptHandler = new AcceptHandler(channel);
            Log.pa("The server is started.");
            channel.accept(null, acceptHandler);
            //noinspection InfiniteLoopStatement
            while (true) Thread.sleep(1000);
        } catch (BindException e) {
            Log.pa("端口已被占用.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
