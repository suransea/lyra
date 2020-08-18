package com.sea.lyrad.stmt;

/**
 * 基础的SQL语句
 * 只包含sql字符串值本身
 * 扩展的sql语句应继承此类并实现SQLStatement接口
 */
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
