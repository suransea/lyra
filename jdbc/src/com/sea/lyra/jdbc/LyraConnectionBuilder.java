package com.sea.lyra.jdbc;

import com.sea.lyra.protocol.lyra.Handler;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.SQLException;

public class LyraConnectionBuilder implements ConnectionBuilder {

    private String username;
    private String password;
    private String url;

    LyraConnectionBuilder(String url) {
        this.url = url;
    }

    @Override
    public ConnectionBuilder user(String username) {
        this.username = username;
        return this;
    }

    @Override
    public ConnectionBuilder password(String password) {
        this.password = password;
        return this;
    }

    @Override
    public Connection build() throws SQLException {
        try {
            url = url.replaceAll("jdbc:", "");
            URL address = new URL(null, url, new Handler());
            URLConnection connection = address.openConnection();
            connection.connect();
            return new LyraConnection(connection, username, password);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            throw new SQLException(e);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
