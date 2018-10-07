package com.sea.lyrad.exec;

import com.sea.lyrad.db.Database;
import com.sea.lyrad.parse.SQLParseException;
import com.sea.lyrad.stmt.PreparedStatement;
import com.sea.lyrad.stmt.SQLStatement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User {
    private String name;
    private Map<Integer, PreparedStatement> preparedStatements;
    private Database currentDB = null;
    private SQLExecutorFactory sqlExecutorFactory;

    public User(String name) {
        this.name = name;
        preparedStatements = new HashMap<>();
        sqlExecutorFactory = new SQLExecutorFactory();
    }

    public String getName() {
        return name;
    }

    public void addPreparedStatement(int hashcode, PreparedStatement preparedStatement) {
        preparedStatements.put(hashcode, preparedStatement);
    }

    public PreparedStatement getPreparedStatement(int hashcode) {
        return preparedStatements.get(hashcode);
    }

    public void removePreparedStatement(int hashcode) {
        preparedStatements.remove(hashcode);
    }

    public Database getCurrentDB() {
        return currentDB;
    }

    public void setCurrentDB(Database currentDB) {
        this.currentDB = currentDB;
    }

    public List<String> getAccessDBNames() {
        DBManager dbManager = DBManager.getInstance();
        List<String> result = dbManager.getDBNames();
        if (!name.equals("root")) {
            result.remove("lyra");
        }
        return result;
    }

    public String execute(SQLStatement statement) throws DBProcessException, SQLParseException {
        SQLExecutor executor = sqlExecutorFactory.createInstance(statement);
        return executor.execute(User.this, statement);
    }
}
