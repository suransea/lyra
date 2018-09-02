package com.sea.pxxd.stmt;

import com.sea.pxxd.DBManager;
import com.sea.pxxd.DBProcessException;
import com.sea.pxxd.User;

import java.util.List;
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
        DBManager dbManager = new DBManager();
        List dbNames = dbManager.getDBNames();
        if (!dbNames.contains(dbName)) {
            throw new DBProcessException("The database is not exist.");
        }
        user.setCurrentDB(dbManager.getDatabase(dbName));
        return "Database changed.";
    }

    @Override
    public String toString() {
        return sql;
    }
}
