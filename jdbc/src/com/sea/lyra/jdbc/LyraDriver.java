package com.sea.lyra.jdbc;

import java.sql.*;
import java.util.Properties;
import java.util.logging.Logger;

public class LyraDriver implements java.sql.Driver {

    static {
        try {
            DriverManager.registerDriver(new LyraDriver());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected LyraDriver() {
    }

    @Override
    public Connection connect(String url, Properties info) throws SQLException {
        if (!acceptsURL(url)) return null;
        ConnectionBuilder builder = new LyraConnectionBuilder(url);
        return builder.user(info.getProperty("user"))
                .password(info.getProperty("password"))
                .build();
    }

    @Override
    public boolean acceptsURL(String url) throws SQLException {
        return url.startsWith("jdbc:lyra://");
    }

    @Override
    public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
        return new DriverPropertyInfo[0];
    }

    @Override
    public int getMajorVersion() {
        return 0;
    }

    @Override
    public int getMinorVersion() {
        return 0;
    }

    @Override
    public boolean jdbcCompliant() {
        return false;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }
}
