package com.sea.lyrad.parse.stmt.ddl;

import com.sea.lyrad.parse.stmt.context.Column;

import java.util.ArrayList;
import java.util.List;

public class CreateStatement extends DDLStatement {
    public CreateStatement(String sql) {
        super(sql);
        columns = new ArrayList<>();
    }

    private List<Column> columns;

    public List<Column> getColumns() {
        return columns;
    }
}
