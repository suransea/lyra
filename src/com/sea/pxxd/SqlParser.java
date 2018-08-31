package com.sea.pxxd;

public class SqlParser {

    public boolean isLastSuccessful() {
        return lastSuccessful;
    }

    private boolean lastSuccessful = false;

    public SqlParser() {

    }

    public String parse(String sql) {
        //TODO: sql解析
        lastSuccessful = true;
        return sql + "\n";
    }
}
