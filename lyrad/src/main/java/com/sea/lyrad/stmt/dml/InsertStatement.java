package com.sea.lyrad.stmt.dml;

import com.sea.lyrad.stmt.common.Column;

import java.util.ArrayList;
import java.util.List;

public class InsertStatement extends DMLStatement {
    private List<Column> columns;
    private List<List<String>> values;

    public InsertStatement(String sql) {
        super(sql);
        columns = new ArrayList<>();
        values = new ArrayList<>();
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

    public int getColumnCount() {
        return columns.size();
    }
}
