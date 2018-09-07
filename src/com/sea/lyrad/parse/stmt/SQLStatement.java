package com.sea.lyrad.parse.stmt;

public abstract class SQLStatement {

    private String sql;

    public SQLStatement(String sql) {
        this.sql = sql;
    }

    @Override
    public String toString() {
        return sql;
    }
}
