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
            private InputStream inputStream = null;
            private OutputStream outputStream = null;

            @Override
            public InputStream getInputStream() throws IOException {
                if (inputStream == null) {
                    inputStream = socket.getInputStream();
                }
                return inputStream;
            }

            @Override
            public OutputStream getOutputStream() throws IOException {
                if (outputStream == null) {
                    outputStream = socket.getOutputStream();
                }
                return outputStream;
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
