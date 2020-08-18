package com.sea.lyra.jdbc;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.sql.*;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

public class LyraConnection implements Connection {
    private URLConnection connection;
    private InputStream inputStream;
    private OutputStream outputStream;

    //内建
    private boolean autoCommit = false;
    private boolean closed = false;
    private boolean readOnly = false;
    private String catalog = "";
    private int transactionIsolation = 0;
    private SQLWarning sqlWarning = null;
    private Map<String, Class<?>> typeMap = null;
    private int holdability = 0;
    private Savepoint savepoint = null;
    private Properties clientInfo = null;
    private String schema = null;


    LyraConnection(URLConnection urlConnection, String user, String password) throws IOException, SQLException {
        this.connection = urlConnection;
        inputStream = connection.getInputStream();
        outputStream = connection.getOutputStream();
        JSONObject login = new JSONObject();
        login.put("tag", "login");
        login.put("user", user);
        login.put("password", password);

        //验证密码
        send(login.toString() + "\n");
        String response = receive();
        JSONObject json = new JSONObject(response);
        if (!json.getBoolean("access")) {
            throw new SQLException("Username or password is not right, permission denied.");
        }

        //切换数据库
        String dbName = connection.getURL().getPath().replaceAll("/", "");
        String useStmt = String.format("use %s", dbName);
        try {
            createStatement().execute(useStmt);
        } catch (SQLException e) {
            throw new SQLException(String.format("The target database [%s] is not exist.", dbName));
        }
    }

    private void send(String content) throws IOException {
        outputStream.write(content.getBytes(Charset.forName("utf-8")));
        outputStream.flush();
    }

    private String receive() throws IOException {
        byte[] receive = new byte[1024];
        inputStream.read(receive);
        return new String(receive, Charset.forName("utf-8"));
    }

    @Override
    public Statement createStatement() throws SQLException {
        try {
            return new LyraStatement(connection, LyraConnection.this);
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        try {
            return new LyraPreparedStatement(connection, LyraConnection.this, sql);
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public CallableStatement prepareCall(String sql) throws SQLException {
        return null;
    }

    @Override
    public String nativeSQL(String sql) throws SQLException {
        return null;
    }

    @Override
    public boolean getAutoCommit() throws SQLException {
        return autoCommit;
    }

    @Override
    public void setAutoCommit(boolean autoCommit) throws SQLException {
        this.autoCommit = autoCommit;
    }

    @Override
    public void commit() throws SQLException {

    }

    @Override
    public void rollback() throws SQLException {

    }

    @Override
    public void close() throws SQLException {
        if (closed) return;
        try {
            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closed = true;
        }
    }

    @Override
    public boolean isClosed() throws SQLException {
        return closed;
    }

    @Override
    public DatabaseMetaData getMetaData() throws SQLException {
        return null;
    }

    @Override
    public boolean isReadOnly() throws SQLException {
        return readOnly;
    }

    @Override
    public void setReadOnly(boolean readOnly) throws SQLException {
        this.readOnly = readOnly;
    }

    @Override
    public String getCatalog() throws SQLException {
        return catalog;
    }

    @Override
    public void setCatalog(String catalog) throws SQLException {
        this.catalog = catalog;
    }

    @Override
    public int getTransactionIsolation() throws SQLException {
        return transactionIsolation;
    }

    @Override
    public void setTransactionIsolation(int level) throws SQLException {
        transactionIsolation = level;
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        return sqlWarning;
    }

    @Override
    public void clearWarnings() throws SQLException {
        sqlWarning = null;
    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
        return null;
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        return null;
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        return null;
    }

    @Override
    public Map<String, Class<?>> getTypeMap() throws SQLException {
        return typeMap;
    }

    @Override
    public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
        this.typeMap = map;
    }

    @Override
    public int getHoldability() throws SQLException {
        return holdability;
    }

    @Override
    public void setHoldability(int holdability) throws SQLException {
        this.holdability = holdability;
    }

    @Override
    public Savepoint setSavepoint() throws SQLException {
        return savepoint;
    }

    @Override
    public Savepoint setSavepoint(String name) throws SQLException {
        return null;
    }

    @Override
    public void rollback(Savepoint savepoint) throws SQLException {

    }

    @Override
    public void releaseSavepoint(Savepoint savepoint) throws SQLException {

    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return null;
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return null;
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return null;
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
        return null;
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
        return null;
    }

    @Override
    public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
        return null;
    }

    @Override
    public Clob createClob() throws SQLException {
        return null;
    }

    @Override
    public Blob createBlob() throws SQLException {
        return null;
    }

    @Override
    public NClob createNClob() throws SQLException {
        return null;
    }

    @Override
    public SQLXML createSQLXML() throws SQLException {
        return null;
    }

    @Override
    public boolean isValid(int timeout) throws SQLException {
        return !closed;
    }

    @Override
    public void setClientInfo(String name, String value) throws SQLClientInfoException {
        clientInfo = new Properties();
        clientInfo.setProperty(name, value);
    }

    @Override
    public String getClientInfo(String name) throws SQLException {
        return clientInfo.toString();
    }

    @Override
    public Properties getClientInfo() throws SQLException {
        return clientInfo;
    }

    @Override
    public void setClientInfo(Properties properties) throws SQLClientInfoException {
        clientInfo = properties;
    }

    @Override
    public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
        return null;
    }

    @Override
    public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
        return null;
    }

    @Override
    public String getSchema() throws SQLException {
        return schema;
    }

    @Override
    public void setSchema(String schema) throws SQLException {
        this.schema = schema;
    }

    @Override
    public void abort(Executor executor) throws SQLException {

    }

    @Override
    public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {

    }

    @Override
    public int getNetworkTimeout() throws SQLException {
        return 0;
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
