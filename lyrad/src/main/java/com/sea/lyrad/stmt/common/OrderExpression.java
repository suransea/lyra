package com.sea.lyrad.stmt.common;

import java.util.ArrayList;
import java.util.List;

/**
 * order表达式
 */
public class OrderExpression {
    private List<Column> columns;
    private boolean asc = true;

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
}
