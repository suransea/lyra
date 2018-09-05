package com.sea.lyrad.stmt;

import com.sea.lyrad.DBManager;
import com.sea.lyrad.DBProcessException;
import com.sea.lyrad.User;
import com.sea.lyrad.db.Table;
import com.sea.lyrad.util.ConsoleTable;

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
        ConsoleTable consoleTable = new ConsoleTable(1);
        consoleTable.appendRow();
        consoleTable.appendColumn("TABLES");
        DBManager dbManager = new DBManager();
        user.setCurrentDB(dbManager.getDatabase(user.getCurrentDB().getName()));
        for (Table table : user.getCurrentDB().getTables()) {
            consoleTable.appendRow();
            consoleTable.appendColumn(table.getName());
        }
        return consoleTable.toString();
    }
}
