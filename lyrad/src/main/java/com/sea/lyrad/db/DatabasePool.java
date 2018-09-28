package com.sea.lyrad.db;

import com.sea.lyrad.exec.DBManager;
import com.sea.lyrad.exec.DBProcessException;

import java.util.Hashtable;
import java.util.Map;

public class DatabasePool {
    private Map<String, Database> pool;

    public DatabasePool() {
        pool = new Hashtable<>();
    }

    public Database getDatabase(String name) throws DBProcessException {
        DBManager dbManager = DBManager.getInstance();
        Database result = pool.get(name);
        if (result == null) {
            Database database = dbManager.takeDatabase(name);
            pool.put(name, database);
            return database;
        }
        return result;
    }
}
