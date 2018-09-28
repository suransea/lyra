package com.sea.lyrad.parse.stmt.dql;

import com.sea.lyrad.parse.stmt.context.Column;
import com.sea.lyrad.parse.stmt.context.OrderExpression;

import java.util.ArrayList;
import java.util.List;

public class SelectStatement extends DQLStatement {
    public SelectStatement(String sql) {
        super(sql);
        columns = new ArrayList<>();
        orderExpression = new OrderExpression();
    }

    private List<Column> columns;

    public List<Column> getColumns() {
        return columns;
    }

    public boolean isStar() {
        return star;
    }

    public void setStar(boolean star) {
        this.star = star;
    }

    private boolean star = false;

    public OrderExpression getOrderExpression() {
        return orderExpression;
    }

    public void setOrderExpression(OrderExpression orderExpression) {
        this.orderExpression = orderExpression;
    }

    private OrderExpression orderExpression;
}
