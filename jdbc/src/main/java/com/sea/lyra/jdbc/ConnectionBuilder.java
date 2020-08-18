package com.sea.lyra.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectionBuilder {
    ConnectionBuilder user(String user);

    ConnectionBuilder password(String password);

    Connection build() throws SQLException;
}
