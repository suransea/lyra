package com.sea.lyrad.db;

import com.sea.lyrad.exec.DBManager;
import com.sea.lyrad.exec.DBProcessException;

import java.util.HashMap;
import java.util.Map;

/**
 * 数据库对象池
 */
public class DatabasePool {
    private Map<String, Database> pool;

    public DatabasePool() {
        pool = new HashMap<>();
    }

    /**
     * 获取数据库对象，若池中有则直接返回，否则创建，入池并返回
     *
     * @param name 数据库名
     * @return 数据库对象
     * @throws DBProcessException 目标数据库不存在
     */
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
