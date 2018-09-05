package com.sea.lyrad.stmt;

import com.sea.lyrad.DBProcessException;
import com.sea.lyrad.User;
import com.sea.lyrad.util.ConsoleTable;

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
        consoleTable.appendColumn("DATABASES");
        for (String name : user.getAccessDBNames()) {
            consoleTable.appendRow();
            consoleTable.appendColumn(name);
        }
        return consoleTable.toString();
    }
}
