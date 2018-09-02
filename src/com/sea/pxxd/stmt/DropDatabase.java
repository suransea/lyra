package com.sea.pxxd.stmt;

import com.sea.pxxd.DBManager;
import com.sea.pxxd.DBProcessException;
import com.sea.pxxd.User;

import java.util.regex.Matcher;

public class DropDatabase implements Statement {

    private String sql;
    private String dbName;

    public DropDatabase(Matcher matcher) {
        sql = matcher.group(0);
        dbName = matcher.group(1);
    }

    @Override
    public String toString() {
        return sql;
    }

    @Override
    public String execute(User user) throws DBProcessException {
        if (!user.getAccessDBNames().contains(dbName)) {
            throw new DBProcessException("The target database is not exist.");
        }
        DBManager dbManager = new DBManager();
        dbManager.deleteDatabase(dbName);
        if (user.getCurrentDB().getName().equals(dbName)) {
            user.setCurrentDB(null);
        }
        return "Deleted.";
    }
}
