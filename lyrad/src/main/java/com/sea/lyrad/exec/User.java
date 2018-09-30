package com.sea.lyrad.exec;

import com.sea.lyrad.db.Database;
import com.sea.lyrad.parse.stmt.PreparedStatement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User {
    public String getName() {
        return name;
    }

    private String name;

    private Map<Integer, PreparedStatement> preparedStatements;

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

    private Database currentDB = null;

    public List<String> getAccessDBNames() {
        DBManager dbManager = DBManager.getInstance();
        List<String> result = dbManager.getDBNames();
        if (!name.equals("root")) {
            result.remove("lyra");
        }
        return result;
    }

    public User(String name) {
        this.name = name;
        preparedStatements = new HashMap<>();
    }
}
