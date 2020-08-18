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
    //连接池
    private static Stack<LyraConnectionWrapper> pool = new Stack<>();
    private String url;
    private String user = "";
    private String password = "";

    public LyraDataSource(String url) {
        this.url = url;
    }

    public LyraDataSource(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return getConnection(user, password);
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        ConnectionBuilder connectionBuilder = new LyraConnectionBuilder(url);
        if (!url.matches("jdbc:lyra://.+:\\d+/\\w+/?")) {
            throw new SQLException(String.format("Error url [%s].", url));
        }
        url = url.replaceAll("jdbc:", "");
        if (!pool.empty()) {
            LyraConnectionWrapper wrapper = pool.pop();
            String useStmt;
            try {
                URL address = new URL(null, url, new com.sea.lyra.protocol.lyra.Handler());
                useStmt = String.format("use %s", address.getPath().replaceAll("/", ""));
            } catch (MalformedURLException e) {
                throw new SQLException("error url.");
            }
            connectionBuilder.user(username).password(password).build().close();//验证密码
            wrapper.createStatement().execute(useStmt);//切换数据库
            return wrapper;
        }
        Connection connection = connectionBuilder
                .user(username)
                .password(password)
                .build();

        //connection wrapper的close方法调用时调用此回调方法释放连接
        Function callback = new Function() {
            @Override
            public void call(Object... objects) {
                pool.push((LyraConnectionWrapper) objects[0]);
            }
        };
        return new LyraConnectionWrapper(connection, callback);
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return null;
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {

    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return 0;
    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {

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
