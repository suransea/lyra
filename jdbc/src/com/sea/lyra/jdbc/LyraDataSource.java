package com.sea.lyra.jdbc;

import javax.sql.DataSource;
import java.io.PrintWriter;
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
    private static Stack<String> urls = new Stack<>();

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
        if (!pool.empty() && urls.peek().equals(url)) {
            urls.pop();
            return pool.pop();
        }
        ConnectionBuilder connectionBuilder = new LyraConnectionBuilder(url);
        Connection connection = connectionBuilder
                .user(username)
                .password(password)
                .build();
        return new LyraConnectionWrapper(connection, x -> {
            pool.push(x);
            urls.push(url);
        });
    }

    public interface CloseAction {
        void act(LyraConnectionWrapper wrapper);
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
