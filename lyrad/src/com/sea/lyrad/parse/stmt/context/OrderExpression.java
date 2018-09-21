package com.sea.lyrad.parse.stmt.context;

import java.util.ArrayList;
import java.util.List;

public class OrderExpression {
    public OrderExpression() {
        columns = new ArrayList<>();
    }

    public List<Column> getColumns() {
        return columns;
    }

    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }

    public boolean isAsc() {
        return asc;
    }

    public void setAsc(boolean asc) {
        this.asc = asc;
    }

    private List<Column> columns;
    private boolean asc = true;
}
