package com.sea.lyrad.server;

import com.sea.lyrad.exec.DBManager;
import com.sea.lyrad.exec.DBProcessException;
import com.sea.lyrad.exec.SQLExecutor;
import com.sea.lyrad.exec.User;
import com.sea.lyrad.lex.Lexer;
import com.sea.lyrad.lex.analyze.UnterminatedCharException;
import com.sea.lyrad.parse.*;
import com.sea.lyrad.parse.stmt.SQLStatement;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;

/**
 * 用来处理客户端请求
 */
public class LyraHandler {
    private OutputStream outputStream;
    private User user = null;
    private SQLExecutor sqlExecutor;

    public LyraHandler(OutputStream outputStream) {
        this.outputStream = outputStream;
        sqlExecutor = new SQLExecutor();
    }

    public void handleLogin(JSONObject request, int count) throws IOException {
        String username = request.getString("user");
        String password = request.getString("password");
        DBManager dbManager = DBManager.getInstance();
        boolean access;
        try {
            access = dbManager.verify(username, password);
        } catch (DBProcessException e) {
            access = false;
        }
        JSONObject response = new JSONObject();
        response.put("access", access);
        if (access) {
            response.put("version", LyraServer.VERSION);
            response.put("count", count);
            user = new User(username);
        }
        send(response.toString());
    }

    public void handleSQL(JSONObject request) throws IOException {
        if (user == null) {
            return;
        }
        String sql = request.getString("sql");
        Lexer lexer = new Lexer(sql);
        SQLParser parser = new SQLParser(lexer);
        JSONObject response = new JSONObject();
        try {
            long startTime = System.currentTimeMillis();
            SQLStatement statement = parser.parse();
            String outcome = sqlExecutor.execute(user, statement);
            long time = System.currentTimeMillis() - startTime;
            response.put("outcome", outcome);
            response.put("time", time);
            response.put("complete", true);
        } catch (DBProcessException | SQLParseException |
                SQLParseUnsupportedException | UnterminatedCharException |
                SQLCompileUnsupportedException e) {
            response.put("outcome", e.getMessage());
            response.put("complete", false);
        }
        outputStream.write(toByteArray(response.toString().getBytes("utf-8").length));
        outputStream.flush();
        send(response.toString());
    }

    public void handlePrepare(JSONObject request) throws IOException {
        if (user == null) {
            return;
        }
        String sql = request.getString("sql");
        int hashcode = request.getInt("hash");
        Lexer lexer = new Lexer(sql);
        SQLCompiler compiler = new SQLCompiler(lexer);
        JSONObject response = new JSONObject();
        try {
            long startTime = System.currentTimeMillis();
            user.addPreparedStatement(hashcode, compiler.compile());
            long time = System.currentTimeMillis() - startTime;
            response.put("time", time);
            response.put("complete", true);
        } catch (SQLParseException | SQLParseUnsupportedException |
                UnterminatedCharException | SQLCompileUnsupportedException e) {
            response.put("outcome", e.getMessage());
            response.put("complete", false);
        }
        outputStream.write(toByteArray(response.toString().getBytes("utf-8").length));
        outputStream.flush();
        send(response.toString());
    }

    public void handleClose(JSONObject request) {
        if (user == null) {
            return;
        }
        int hashcode = request.getInt("hash");
        user.removePreparedStatement(hashcode);
    }

    public void handleExecute(JSONObject request) throws IOException {
        if (user == null) {
            return;
        }
        int hashcode = request.getInt("hash");
        JSONArray params = new JSONArray(request.getString("params"));
        JSONObject response = new JSONObject();
        try {
            long startTime = System.currentTimeMillis();
            SQLStatement statement = user.getPreparedStatement(hashcode).toSQLStatement(params);
            String outcome = sqlExecutor.execute(user, statement);
            long time = System.currentTimeMillis() - startTime;
            response.put("outcome", outcome);
            response.put("time", time);
            response.put("complete", true);
        } catch (DBProcessException | SQLParseException e) {
            response.put("outcome", e.getMessage());
            response.put("complete", false);
        }
        outputStream.write(toByteArray(response.toString().getBytes("utf-8").length));
        outputStream.flush();
        send(response.toString());
    }

    private void send(String response) throws IOException {
        outputStream.write(response.getBytes("utf-8"));
        outputStream.flush();
    }

    private byte[] toByteArray(int integer) {
        byte[] bytes = new byte[4];
        bytes[0] = (byte) ((integer >> 24) & 0xff);
        bytes[1] = (byte) ((integer >> 16) & 0xff);
        bytes[2] = (byte) ((integer >> 8) & 0xff);
        bytes[3] = (byte) (integer & 0xff);
        return bytes;
    }
}
