package com.sea.lyrad.server;

import com.sea.lyrad.compile.SQLCompileUnsupportedException;
import com.sea.lyrad.compile.SQLCompiler;
import com.sea.lyrad.compile.SQLCompilerFactory;
import com.sea.lyrad.exec.DBManager;
import com.sea.lyrad.exec.DBProcessException;
import com.sea.lyrad.exec.User;
import com.sea.lyrad.lex.analyze.UnterminatedCharException;
import com.sea.lyrad.parse.SQLParseException;
import com.sea.lyrad.parse.SQLParseUnsupportedException;
import com.sea.lyrad.parse.SQLParser;
import com.sea.lyrad.parse.SQLParserFactory;
import com.sea.lyrad.stmt.SQLStatement;
import com.sea.lyrad.util.IntegerUtil;
import org.json.JSONArray;
import org.json.JSONObject;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.charset.StandardCharsets;

/**
 * 用来处理客户端请求
 */
public class LyraHandler {
    private AsynchronousSocketChannel channel;
    private User user = null;
    private SQLParserFactory sqlParserFactory;
    private SQLCompilerFactory sqlCompilerFactory;

    public LyraHandler(AsynchronousSocketChannel channel) {
        this.channel = channel;
        sqlParserFactory = new SQLParserFactory();
        sqlCompilerFactory = new SQLCompilerFactory();
    }

    public void handle(String request) {
        JSONObject json = new JSONObject(request);
        switch (json.getString("tag")) {
            case "login": {
                handleLogin(json, AcceptHandler.getConnectionId());
                break;
            }
            case "sql": {
                handleSQL(json); //处理普通SQL执行
                break;
            }
            case "pre": {
                handlePrepare(json);
                break;
            }
            case "exec": {
                handleExecute(json); //处理prepared SQL的执行
                break;
            }
            case "close": {
                handleClose(json);
                break;
            }
        }
    }

    /**
     * 处理登录请求
     *
     * @param request 请求JSON
     * @param count   当前连接ID
     */
    public void handleLogin(JSONObject request, int count) {
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

    /**
     * 处理sql请求
     *
     * @param request 请求JSON
     */
    public void handleSQL(JSONObject request) {
        if (user == null) {
            return;
        }
        String sql = request.getString("sql");
        JSONObject response = new JSONObject();
        try {
            long startTime = System.currentTimeMillis();
            SQLParser parser = sqlParserFactory.createInstance(sql);
            SQLStatement statement = parser.parse();
            String outcome = user.execute(statement);
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
        sendLength(response.toString());
        send(response.toString());
    }

    /**
     * 处理prepare请求，即编译SQL语句
     *
     * @param request 请求JSON
     */
    public void handlePrepare(JSONObject request) {
        if (user == null) {
            return;
        }
        String sql = request.getString("sql");
        int hashcode = request.getInt("hash");
        JSONObject response = new JSONObject();
        try {
            long startTime = System.currentTimeMillis();
            SQLCompiler compiler = sqlCompilerFactory.createInstance(sql);
            user.addPreparedStatement(hashcode, compiler.compile());
            long time = System.currentTimeMillis() - startTime;
            response.put("time", time);
            response.put("complete", true);
        } catch (SQLParseException | SQLParseUnsupportedException |
                UnterminatedCharException | SQLCompileUnsupportedException e) {
            response.put("outcome", e.getMessage());
            response.put("complete", false);
        }
        sendLength(response.toString());
        send(response.toString());
    }

    /**
     * 处理关闭statement请求
     *
     * @param request 请求JSON
     */
    public void handleClose(JSONObject request) {
        if (user == null) {
            return;
        }
        int hashcode = request.getInt("hash");
        user.removePreparedStatement(hashcode);
    }

    /**
     * 处理prepared语句的执行请求
     *
     * @param request 请求JSON
     */
    public void handleExecute(JSONObject request) {
        if (user == null) {
            return;
        }
        int hashcode = request.getInt("hash");
        JSONArray params = new JSONArray(request.getString("params"));
        JSONObject response = new JSONObject();
        try {
            long startTime = System.currentTimeMillis();
            SQLStatement statement = user.getPreparedStatement(hashcode).toSQLStatement(params);
            String outcome = user.execute(statement);
            long time = System.currentTimeMillis() - startTime;
            response.put("outcome", outcome);
            response.put("time", time);
            response.put("complete", true);
        } catch (DBProcessException | SQLParseException e) {
            response.put("outcome", e.getMessage());
            response.put("complete", false);
        }
        sendLength(response.toString());
        send(response.toString());
    }

    private void send(String response) {
        ByteBuffer buffer = ByteBuffer.wrap(response.getBytes(StandardCharsets.UTF_8));
        channel.write(buffer);
    }

    private void sendLength(String response) {
        ByteBuffer buffer = ByteBuffer.wrap(IntegerUtil.toByteArray(response.getBytes(StandardCharsets.UTF_8).length));
        channel.write(buffer);
    }
}
