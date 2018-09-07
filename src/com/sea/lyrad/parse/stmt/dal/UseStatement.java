package com.sea.lyrad.parse.stmt.dal;

public class UseStatement extends DALStatement {
    private String sql;
    private String dbName;

    public String getDBName() {
        return dbName;
    }

    public UseStatement(String sql, String dbName) {
        this.sql = sql;
        this.dbName = dbName;
    }

    @Override
    public String toString() {
        return sql;
    }
}
