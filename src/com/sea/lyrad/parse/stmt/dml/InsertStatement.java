package com.sea.lyrad.parse.stmt.dml;

import com.sea.lyrad.parse.stmt.context.Column;

import java.util.ArrayList;
import java.util.List;

public class InsertStatement extends DMLStatement {
    public InsertStatement(String sql) {
        super(sql);
        columns = new ArrayList<>();
        values = new ArrayList<>();
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<Column> getColumns() {
        return columns;
    }

    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }

    public List<List<String>> getValues() {
        return values;
    }

    public void setValues(List<List<String>> values) {
        this.values = values;
    }

    private String tableName;
    private List<Column> columns;
    private List<List<String>> values;

    public int getColumnCount() {
        return columns.size();
    }
}
