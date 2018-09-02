package com.sea.pxxd;

import com.sea.pxxd.db.Database;

import java.util.List;

public class User {
    public String getName() {
        return name;
    }

    private String name;

    public Database getCurrentDB() {
        return currentDB;
    }

    public void setCurrentDB(Database currentDB) {
        this.currentDB = currentDB;
    }

    private Database currentDB = null;

    public List<String> getAccessDBNames() {
        DBManager dbManager = new DBManager();
        List<String> result = dbManager.getDBNames();
        if (!name.equals("root")) {
            result.remove("pxx");
        }
        return result;
    }

    public User(String name) {
        this.name = name;
    }
}
