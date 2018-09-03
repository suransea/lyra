package com.sea.pxxd.stmt;

import com.sea.pxxd.DBProcessException;
import com.sea.pxxd.User;
import com.sea.pxxd.util.ConsoleTable;

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
        ConsoleTable consoleTable = new ConsoleTable(1);
        consoleTable.appendRow();
        consoleTable.appendColumn("*DATABASES*");
        for (String name : user.getAccessDBNames()) {
            consoleTable.appendRow();
            consoleTable.appendColumn(name);
        }
        return consoleTable.toString();
    }
}
