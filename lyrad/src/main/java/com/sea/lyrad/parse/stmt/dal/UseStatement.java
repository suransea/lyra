package com.sea.lyrad.parse.stmt.dal;

public class UseStatement extends DALStatement {

    public void setDBName(String dbName) {
        this.dbName = dbName;
    }

    private String dbName;

    public String getDBName() {
        return dbName;
    }

    public UseStatement(String sql) {
        super(sql);
    }
}
