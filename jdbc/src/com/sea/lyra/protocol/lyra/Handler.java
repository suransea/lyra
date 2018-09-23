package com.sea.lyra.protocol.lyra;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;

public class Handler extends URLStreamHandler {
    @Override
    protected URLConnection openConnection(URL url) throws IOException {
        return new URLConnection(url) {
            private Socket socket;

            @Override
            public InputStream getInputStream() throws IOException {
                return socket.getInputStream();
            }

            @Override
            public OutputStream getOutputStream() throws IOException {
                return socket.getOutputStream();
            }

            @Override
            public void connect() throws IOException {
                socket = new Socket();
                SocketAddress address = new InetSocketAddress(url.getHost(), url.getPort());
                socket.connect(address);
            }
        };
    }
}
