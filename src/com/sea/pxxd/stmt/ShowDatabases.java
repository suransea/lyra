package com.sea.pxxd.stmt;

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
        List<String> names = user.getAccessDBNames();
        for (String name : names) {
            result.append('\n');
            result.append(name);
        }
        return result.toString();
    }
}
