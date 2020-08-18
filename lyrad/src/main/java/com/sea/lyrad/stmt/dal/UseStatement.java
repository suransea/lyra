package com.sea.lyrad.stmt.dal;

public class UseStatement extends DALStatement {

    private String dbName;

    public UseStatement(String sql) {
        super(sql);
    }

    public String getDBName() {
        return dbName;
    }

    public void setDBName(String dbName) {
        this.dbName = dbName;
    }
}
