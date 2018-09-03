package com.sea.pxxd.stmt;

import com.sea.pxxd.DBManager;
import com.sea.pxxd.DBProcessException;
import com.sea.pxxd.User;
import com.sea.pxxd.db.Table;
import com.sea.pxxd.util.ConsoleTable;

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
