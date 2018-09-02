package com.sea.pxxd;

import com.sea.pxxd.db.Database;

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

    public User(String name) {
        this.name = name;
    }
}
