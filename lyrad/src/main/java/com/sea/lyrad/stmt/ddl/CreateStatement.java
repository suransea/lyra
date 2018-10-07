package com.sea.lyrad.stmt.ddl;

import com.sea.lyrad.stmt.common.Column;

import java.util.ArrayList;
import java.util.List;

public class CreateStatement extends DDLStatement {
    private List<Column> columns;

    public CreateStatement(String sql) {
        super(sql);
        columns = new ArrayList<>();
    }

    public List<Column> getColumns() {
        return columns;
    }
}
