package com.sea.lyrad.exec;

import com.sea.lyrad.db.Table;
import com.sea.lyrad.lex.token.Keyword;
import com.sea.lyrad.parse.stmt.dal.DALStatement;
import com.sea.lyrad.parse.stmt.dal.ShowStatement;
import com.sea.lyrad.parse.stmt.dal.UseStatement;
import com.sea.lyrad.util.ConsoleTable;

public class DALExecutor extends SQLExecutor {

    private User user;
    private DALStatement statement;

    public String execute(User user, DALStatement statement) throws DBProcessException {
        this.user = user;
        this.statement = statement;
        if (statement instanceof UseStatement) {
            return executeUse();
        } else if (statement instanceof ShowStatement) {
            return executeShow();
        }
        throw new DBProcessException("Unsupported DAL statement.");
    }

    private String executeUse() throws DBProcessException {
        UseStatement stmt = (UseStatement) statement;
        if (!user.getAccessDBNames().contains(stmt.getDBName())) {
            throw new DBProcessException("The database is not exist.");
        }
        DBManager dbManager = new DBManager();
        user.setCurrentDB(dbManager.getDatabase(stmt.getDBName()));
        return "Database changed.";
    }

    private String executeShow() throws DBProcessException {
        ShowStatement stmt = (ShowStatement) statement;
        if (stmt.getItem().equals(Keyword.DATABASES)) {
            ConsoleTable consoleTable = new ConsoleTable(1);
            consoleTable.appendRow();
            consoleTable.appendColumn("DATABASES");
            for (String name : user.getAccessDBNames()) {
                consoleTable.appendRow();
                consoleTable.appendColumn(name);
            }
            return consoleTable.toString();
        } else if (stmt.getItem().equals(Keyword.TABLES)) {
            if (user.getCurrentDB() == null) {
                throw new DBProcessException("Please select a database firstly.");
            }
            ConsoleTable consoleTable = new ConsoleTable(1);
            consoleTable.appendRow();
            consoleTable.appendColumn("TABLES");
            DBManager dbManager = new DBManager();
            user.setCurrentDB(dbManager.getDatabase(user.getCurrentDB().getName()));//刷新database对象
            for (Table table : user.getCurrentDB().getTables()) {
                consoleTable.appendRow();
                consoleTable.appendColumn(table.getName());
            }
            return consoleTable.toString();
        }
        throw new DBProcessException("Unknown error.");
    }
}
