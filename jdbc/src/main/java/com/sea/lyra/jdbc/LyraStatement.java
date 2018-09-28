package com.sea.lyra.jdbc;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;

public class LyraStatement implements Statement {
    private Connection connection;
    private InputStream inputStream;
    private OutputStream outputStream;

    private boolean closed = false;
    private ResultSet resultSet = null;
    private int updateCount = 0;

    private void send(String content) throws IOException {
        outputStream.write(content.getBytes(Charset.forName("utf-8")));
        outputStream.flush();
    }

    private String receive() throws IOException {
        byte[] receive = new byte[4];
        inputStream.read(receive);
        int size = ((receive[0] & 0xff) << 24)
                | ((receive[1] & 0xff) << 16)
                | ((receive[2] & 0xff) << 8)
                | (receive[3] & 0xff);
        int receivedSize = 0;
        while (receivedSize < size) {
            byte[] data = new byte[1048576];
            int length = inputStream.read(data);
            byte[] newReceive = new byte[receivedSize + length];
            System.arraycopy(receive, 0, newReceive, 0, receivedSize);
            System.arraycopy(data, 0, newReceive, receivedSize, length);
            receive = newReceive;
            receivedSize += length;
        }
        return new String(receive, Charset.forName("utf-8"));
    }

    LyraStatement(URLConnection urlConnection, Connection connection) throws IOException {
        inputStream = urlConnection.getInputStream();
        outputStream = urlConnection.getOutputStream();
        this.connection = connection;
    }

    @Override
    public ResultSet executeQuery(String sql) throws SQLException {
        execute(sql);
        return resultSet;
    }

    @Override
    public int executeUpdate(String sql) throws SQLException {
        execute(sql);
        return updateCount;
    }

    @Override
    public void close() throws SQLException {
        closed = true;
    }

    @Override
    public int getMaxFieldSize() throws SQLException {
        return 0;
    }

    @Override
    public void setMaxFieldSize(int max) throws SQLException {

    }

    @Override
    public int getMaxRows() throws SQLException {
        return 0;
    }

    @Override
    public void setMaxRows(int max) throws SQLException {

    }

    @Override
    public void setEscapeProcessing(boolean enable) throws SQLException {

    }

    @Override
    public int getQueryTimeout() throws SQLException {
        return 0;
    }

    @Override
    public void setQueryTimeout(int seconds) throws SQLException {

    }

    @Override
    public void cancel() throws SQLException {

    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        return null;
    }

    @Override
    public void clearWarnings() throws SQLException {

    }

    @Override
    public void setCursorName(String name) throws SQLException {

    }

    @Override
    public boolean execute(String sql) throws SQLException {
        if (!sql.endsWith(";")) sql += ";";
        JSONObject request = new JSONObject();
        request.put("tag", "sql");
        request.put("sql", sql);
        String response;
        try {
            send(request.toString() + "\n");
            response = receive();
        } catch (IOException e) {
            throw new SQLException(e);
        }
        JSONObject json = new JSONObject(response);
        String outcome = json.getString("outcome");
        if (json.getBoolean("complete")) {
            try {
                JSONArray array = new JSONArray(outcome);
                resultSet = new LyraResultSet(array);
            } catch (JSONException e) {
                resultSet = null;
            }
            try {
                Scanner scanner = new Scanner(outcome);
                updateCount = scanner.nextInt();
            } catch (InputMismatchException e) {
                updateCount = 0;
            }
            return true;
        } else {
            throw new SQLException(outcome);
        }
    }

    @Override
    public ResultSet getResultSet() throws SQLException {
        return resultSet;
    }

    @Override
    public int getUpdateCount() throws SQLException {
        return updateCount;
    }

    @Override
    public boolean getMoreResults() throws SQLException {
        return false;
    }

    @Override
    public void setFetchDirection(int direction) throws SQLException {

    }

    @Override
    public int getFetchDirection() throws SQLException {
        return 0;
    }

    @Override
    public void setFetchSize(int rows) throws SQLException {

    }

    @Override
    public int getFetchSize() throws SQLException {
        return 0;
    }

    @Override
    public int getResultSetConcurrency() throws SQLException {
        return 0;
    }

    @Override
    public int getResultSetType() throws SQLException {
        return 0;
    }

    @Override
    public void addBatch(String sql) throws SQLException {

    }

    @Override
    public void clearBatch() throws SQLException {

    }

    @Override
    public int[] executeBatch() throws SQLException {
        return new int[0];
    }

    @Override
    public Connection getConnection() throws SQLException {
        return connection;
    }

    @Override
    public boolean getMoreResults(int current) throws SQLException {
        return false;
    }

    @Override
    public ResultSet getGeneratedKeys() throws SQLException {
        return null;
    }

    @Override
    public int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
        return 0;
    }

    @Override
    public int executeUpdate(String sql, int[] columnIndexes) throws SQLException {
        return 0;
    }

    @Override
    public int executeUpdate(String sql, String[] columnNames) throws SQLException {
        return 0;
    }

    @Override
    public boolean execute(String sql, int autoGeneratedKeys) throws SQLException {
        return false;
    }

    @Override
    public boolean execute(String sql, int[] columnIndexes) throws SQLException {
        return false;
    }

    @Override
    public boolean execute(String sql, String[] columnNames) throws SQLException {
        return false;
    }

    @Override
    public int getResultSetHoldability() throws SQLException {
        return 0;
    }

    @Override
    public boolean isClosed() throws SQLException {
        return closed;
    }

    @Override
    public void setPoolable(boolean poolable) throws SQLException {

    }

    @Override
    public boolean isPoolable() throws SQLException {
        return false;
    }

    @Override
    public void closeOnCompletion() throws SQLException {
        closed = true;
    }

    @Override
    public boolean isCloseOnCompletion() throws SQLException {
        return closed;
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
