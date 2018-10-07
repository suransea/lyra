package com.sea.lyrad.stmt;

public abstract class BaseSQLStatement {
    private String sql;

    public BaseSQLStatement(String sql) {
        this.sql = sql;
    }

    @Override
    public String toString() {
        return sql;
    }
}
