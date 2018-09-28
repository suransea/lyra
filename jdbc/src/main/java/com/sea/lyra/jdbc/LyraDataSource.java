package com.sea.lyra.jdbc;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Stack;
import java.util.logging.Logger;

public class LyraDataSource implements DataSource {
    private String url = "";
    private String user = "";
    private String password = "";
    private String driverClassName = "";

    public LyraDataSource() {
    }

    private static Stack<LyraConnectionWrapper> pool = new Stack<>();

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return getConnection(user, password);
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        if (!url.matches("jdbc:lyra://.+:\\d+/\\w+/?")) {
            throw new SQLException(String.format("Error url [%s].", url));
        }
        url = url.replaceAll("jdbc:", "");
        if (!pool.empty()) {
            LyraConnectionWrapper wrapper = pool.pop();
            String use = "use ";
            try {
                URL address = new URL(null, url, new com.sea.lyra.protocol.lyra.Handler());
                use += address.getPath().replaceAll("/", "");
            } catch (MalformedURLException e) {
                throw new SQLException("error url.");
            }
            wrapper.createStatement().execute(use);
            return wrapper;
        }
        ConnectionBuilder connectionBuilder = new LyraConnectionBuilder(url);
        Connection connection = connectionBuilder
                .user(username)
                .password(password)
                .build();
        Callback callback = x -> pool.push((LyraConnectionWrapper) x);
        return new LyraConnectionWrapper(connection, callback);
    }

    interface Callback {
        void run(Object obj);
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return null;
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {

    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {

    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return 0;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }
}
