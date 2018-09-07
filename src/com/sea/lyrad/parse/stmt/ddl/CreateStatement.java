package com.sea.lyrad.parse.stmt.ddl;

import com.sea.lyrad.lex.token.Keyword;
import com.sea.lyrad.parse.stmt.context.Column;

import java.util.ArrayList;
import java.util.List;

public class CreateStatement extends DDLStatement {
    public CreateStatement(String sql) {
        super(sql);
        columns = new ArrayList<>();
    }

    public Keyword getItem() {
        return item;
    }

    public void setItem(Keyword item) {
        this.item = item;
    }

    private Keyword item;
    private String tableName;
    private String dbName;

    public String getDBName() {
        return dbName;
    }

    public void setDBName(String dbName) {
        this.dbName = dbName;
    }

    private List<Column> columns;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<Column> getColumns() {
        return columns;
    }
}
