package com.sea.lyrad.stmt;

import com.sea.lyrad.DBManager;
import com.sea.lyrad.DBProcessException;
import com.sea.lyrad.User;

import java.util.regex.Matcher;

public class UseDatabase implements Statement {

    private String sql;
    private String dbName;

    public UseDatabase(Matcher matcher) {
        sql = matcher.group(0);
        dbName = matcher.group(1);
    }

    @Override
    public String execute(User user) throws DBProcessException {
        if (!user.getAccessDBNames().contains(dbName)) {
            throw new DBProcessException("The database is not exist.");
        }
        DBManager dbManager = new DBManager();
        user.setCurrentDB(dbManager.getDatabase(dbName));
        return "Database changed.";
    }

    @Override
    public String toString() {
        return sql;
    }
}
