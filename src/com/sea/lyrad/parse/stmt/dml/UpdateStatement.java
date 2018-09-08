package com.sea.lyrad.parse.stmt.dml;

import com.sea.lyrad.parse.stmt.context.Column;

import java.util.ArrayList;
import java.util.List;

public class UpdateStatement extends DMLStatement {
    public List<Column> getColumns() {
        return columns;
    }

    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }

    public UpdateStatement(String sql) {
        super(sql);
        columns = new ArrayList<>();
    }

    private List<Column> columns;
}
