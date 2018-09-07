package com.sea.lyrad.parse.stmt.ddl;

import com.sea.lyrad.lex.token.Keyword;

public class DropStatement extends DDLStatement {
    public DropStatement(String sql) {
        super(sql);
    }

    private Keyword item;
    private String dbName;
    private String tableName;

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
