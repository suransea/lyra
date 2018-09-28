package com.sea.lyrad.exec;

import com.sea.lyrad.db.table.Table;
import com.sea.lyrad.lex.token.Keyword;
import com.sea.lyrad.parse.stmt.dal.DALStatement;
import com.sea.lyrad.parse.stmt.dal.ShowStatement;
import com.sea.lyrad.parse.stmt.dal.UseStatement;
import org.json.JSONArray;

import java.util.Collections;

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
        DBManager dbManager = DBManager.getInstance();
        user.setCurrentDB(dbManager.getDatabase(stmt.getDBName()));
        return "Database changed.";
    }

    private String executeShow() throws DBProcessException {
        ShowStatement stmt = (ShowStatement) statement;
        if (stmt.getItem().equals(Keyword.DATABASES)) {
            JSONArray result = new JSONArray();
            result.put(Collections.singletonList("DATABASES"));
            for (String name : user.getAccessDBNames()) {
                result.put(Collections.singletonList(name));
            }
            return result.toString();
        } else if (stmt.getItem().equals(Keyword.TABLES)) {
            if (user.getCurrentDB() == null) {
                throw new DBProcessException("Please select a database firstly.");
            }
            JSONArray result = new JSONArray();
            result.put(Collections.singletonList("TABLES"));
            for (Table table : user.getCurrentDB().getTables()) {
                result.put(Collections.singletonList(table.getName()));
            }
            return result.toString();
        }
        throw new DBProcessException("Unknown error.");
    }
}
