package com.sea.lyrad.stmt.ddl;

import com.sea.lyrad.lex.token.Keyword;
import com.sea.lyrad.stmt.BaseSQLStatement;
import com.sea.lyrad.stmt.SQLStatement;

public class DDLStatement extends BaseSQLStatement implements SQLStatement {
    private Keyword item;//value is database or table
    private String dbName;
    private String tableName;

    public DDLStatement(String sql) {
        super(sql);
    }

    public String getDBName() {
        return dbName;
    }

    public void setDBName(String dbName) {
        this.dbName = dbName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Keyword getItem() {
        return item;
    }

    public void setItem(Keyword item) {
        this.item = item;
    }
}
