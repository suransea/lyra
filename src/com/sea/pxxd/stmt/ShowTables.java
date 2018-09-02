package com.sea.pxxd.stmt;

import com.sea.pxxd.DBProcessException;
import com.sea.pxxd.User;
import com.sea.pxxd.db.Table;

import java.util.regex.Matcher;

public class ShowTables implements Statement {

    private String sql;

    public ShowTables(Matcher matcher) {
        sql = matcher.group(0);
    }

    @Override
    public String toString() {
        return sql;
    }

    @Override
    public String execute(User user) throws DBProcessException {
        if (user.getCurrentDB() == null) {
            throw new DBProcessException("Please select a database firstly.");
        }
        StringBuilder result = new StringBuilder();
        for (Table table : user.getCurrentDB().getTables()) {
            result.append("\n");
            result.append(table.getName());
        }
        return result.toString();
    }
}
