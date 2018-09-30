package com.sea.lyra.jdbc;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class LyraPreparedStatement extends LyraStatement implements PreparedStatement {
    private String sql;
    List<String> params;

    LyraPreparedStatement(URLConnection urlConnection, Connection connection, String sql) throws IOException, SQLException {
        super(urlConnection, connection);
        this.sql = sql;
        init();
    }

    @Override
    public void close() throws SQLException {
        JSONObject request = new JSONObject();
        request.put("tag", "close");
        request.put("hash", this.hashCode());
        try {
            send(request.toString() + "\n");
        } catch (IOException e) {
            throw new SQLException(e);
        } finally {
            super.close();
        }
    }

    private void init() throws SQLException {
        if (!sql.endsWith(";")) sql += ";";
        int count = sql.length() - sql.replaceAll("\\?", "").length();
        params = new ArrayList<>(Collections.nCopies(count, ""));
        JSONObject request = new JSONObject();
        request.put("tag", "pre");
        request.put("sql", sql);
        request.put("hash", this.hashCode());
        String response;
        try {
            send(request.toString() + "\n");
            response = receive();
        } catch (IOException e) {
            throw new SQLException(e);
        }
        JSONObject json = new JSONObject(response);
        if (!json.getBoolean("complete")) {
            String outcome = json.getString("outcome");
            throw new SQLException(String.format("Cannot compile SQL statement: %s", outcome));
        }
    }

    @Override
    public ResultSet executeQuery() throws SQLException {
        execute();
        return resultSet;
    }

    @Override
    public int executeUpdate() throws SQLException {
        execute();
        return updateCount;
    }

    @Override
    public void setNull(int parameterIndex, int sqlType) throws SQLException {
    }

    @Override
    public void setBoolean(int parameterIndex, boolean x) throws SQLException {

    }

    @Override
    public void setByte(int parameterIndex, byte x) throws SQLException {

    }

    @Override
    public void setShort(int parameterIndex, short x) throws SQLException {

    }

    @Override
    public void setInt(int parameterIndex, int x) throws SQLException {
        try {
            params.set(--parameterIndex, String.valueOf(x));
        } catch (IndexOutOfBoundsException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void setLong(int parameterIndex, long x) throws SQLException {

    }

    @Override
    public void setFloat(int parameterIndex, float x) throws SQLException {

    }

    @Override
    public void setDouble(int parameterIndex, double x) throws SQLException {

    }

    @Override
    public void setBigDecimal(int parameterIndex, BigDecimal x) throws SQLException {

    }

    @Override
    public void setString(int parameterIndex, String x) throws SQLException {
        try {
            params.set(--parameterIndex, x);
        } catch (IndexOutOfBoundsException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void setBytes(int parameterIndex, byte[] x) throws SQLException {

    }

    @Override
    public void setDate(int parameterIndex, Date x) throws SQLException {

    }

    @Override
    public void setTime(int parameterIndex, Time x) throws SQLException {

    }

    @Override
    public void setTimestamp(int parameterIndex, Timestamp x) throws SQLException {

    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x, int length) throws SQLException {

    }

    @Override
    public void setUnicodeStream(int parameterIndex, InputStream x, int length) throws SQLException {

    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x, int length) throws SQLException {

    }

    @Override
    public void clearParameters() throws SQLException {

    }

    @Override
    public void setObject(int parameterIndex, Object x, int targetSqlType) throws SQLException {

    }

    @Override
    public void setObject(int parameterIndex, Object x) throws SQLException {

    }

    @Override
    public boolean execute() throws SQLException {
        JSONObject request = new JSONObject();
        request.put("tag", "exec");
        request.put("sql", sql);
        request.put("hash", this.hashCode());
        JSONArray paramsJSON = new JSONArray();
        params.forEach(paramsJSON::put);
        request.put("params", paramsJSON.toString());
        String response;
        try {
            send(request.toString() + "\n");
            response = receive();
        } catch (IOException e) {
            throw new SQLException(e);
        }
        JSONObject json = new JSONObject(response);
        return getOutcome(json);
    }

    @Override
    public void addBatch() throws SQLException {

    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader reader, int length) throws SQLException {

    }

    @Override
    public void setRef(int parameterIndex, Ref x) throws SQLException {

    }

    @Override
    public void setBlob(int parameterIndex, Blob x) throws SQLException {

    }

    @Override
    public void setClob(int parameterIndex, Clob x) throws SQLException {

    }

    @Override
    public void setArray(int parameterIndex, Array x) throws SQLException {

    }

    @Override
    public ResultSetMetaData getMetaData() throws SQLException {
        return null;
    }

    @Override
    public void setDate(int parameterIndex, Date x, Calendar cal) throws SQLException {

    }

    @Override
    public void setTime(int parameterIndex, Time x, Calendar cal) throws SQLException {

    }

    @Override
    public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal) throws SQLException {

    }

    @Override
    public void setNull(int parameterIndex, int sqlType, String typeName) throws SQLException {

    }

    @Override
    public void setURL(int parameterIndex, URL x) throws SQLException {

    }

    @Override
    public ParameterMetaData getParameterMetaData() throws SQLException {
        return null;
    }

    @Override
    public void setRowId(int parameterIndex, RowId x) throws SQLException {

    }

    @Override
    public void setNString(int parameterIndex, String value) throws SQLException {

    }

    @Override
    public void setNCharacterStream(int parameterIndex, Reader value, long length) throws SQLException {

    }

    @Override
    public void setNClob(int parameterIndex, NClob value) throws SQLException {

    }

    @Override
    public void setClob(int parameterIndex, Reader reader, long length) throws SQLException {

    }

    @Override
    public void setBlob(int parameterIndex, InputStream inputStream, long length) throws SQLException {

    }

    @Override
    public void setNClob(int parameterIndex, Reader reader, long length) throws SQLException {

    }

    @Override
    public void setSQLXML(int parameterIndex, SQLXML xmlObject) throws SQLException {

    }

    @Override
    public void setObject(int parameterIndex, Object x, int targetSqlType, int scaleOrLength) throws SQLException {

    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x, long length) throws SQLException {

    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x, long length) throws SQLException {

    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader reader, long length) throws SQLException {

    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x) throws SQLException {

    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x) throws SQLException {

    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader reader) throws SQLException {

    }

    @Override
    public void setNCharacterStream(int parameterIndex, Reader value) throws SQLException {

    }

    @Override
    public void setClob(int parameterIndex, Reader reader) throws SQLException {

    }

    @Override
    public void setBlob(int parameterIndex, InputStream inputStream) throws SQLException {

    }

    @Override
    public void setNClob(int parameterIndex, Reader reader) throws SQLException {

    }
}