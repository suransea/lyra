package com.sea.lyrad.stmt.dml;

import com.sea.lyrad.stmt.common.Column;

import java.util.ArrayList;
import java.util.List;

public class UpdateStatement extends DMLStatement {
    private List<Column> columns;

    public UpdateStatement(String sql) {
        super(sql);
        columns = new ArrayList<>();
    }

    public List<Column> getColumns() {
        return columns;
    }

    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }
}
