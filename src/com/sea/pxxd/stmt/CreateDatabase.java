package com.sea.pxxd.stmt;

import com.sea.pxxd.DBManager;
import com.sea.pxxd.DBProcessException;

import java.util.regex.Matcher;

public class CreateDatabase implements Statement {

    public String getDbName() {
        return dbName;
    }

    private String dbName;
    private String sql;

    public CreateDatabase(Matcher matcher) {
        sql = matcher.group(0);
        dbName = matcher.group(1);
    }

    @Override
    public String execute(DBManager dbManager) throws DBProcessException {
        dbManager = new DBManager(dbName);
        return dbManager.CreateDatabase();
    }

    @Override
    public String toString() {
        return sql;
    }
}
