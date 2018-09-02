package com.sea.pxxd.stmt;

import com.sea.pxxd.DBManager;
import com.sea.pxxd.DBProcessException;
import com.sea.pxxd.User;

import java.util.List;
import java.util.regex.Matcher;

public class ShowDatabases implements Statement {

    private String sql;

    public ShowDatabases(Matcher matcher) {
        sql = matcher.group(0);
    }

    @Override
    public String toString() {
        return sql;
    }

    @Override
    public String execute(User user) throws DBProcessException {
        StringBuilder result = new StringBuilder();
        DBManager dbManager = new DBManager();
        List<String> names = dbManager.getDBNames();
        for (String name : names) {
            if (!user.getName().equals("root") && name.equals("pxx")) continue;
            result.append('\n');
            result.append(name);
        }
        return result.toString();
    }
}
