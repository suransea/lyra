package com.sea.lyrad.stmt.dql;

import com.sea.lyrad.lex.token.Keyword;
import com.sea.lyrad.parse.SQLParseException;
import com.sea.lyrad.stmt.common.Column;
import com.sea.lyrad.stmt.common.Condition;
import com.sea.lyrad.stmt.common.OrderExpression;
import com.sea.lyrad.stmt.common.WhereExpression;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SelectStatement extends DQLStatement {
    private List<Column> columns;
    private boolean star = false;//select语句中的星号
    private String tableName;
    private OrderExpression orderExpression;
    private WhereExpression whereExpression;

    public SelectStatement(String sql) {
        super(sql);
        columns = new ArrayList<>();
        orderExpression = new OrderExpression();
        whereExpression = new WhereExpression();
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public WhereExpression getWhereExpression() {
        return whereExpression;
    }

    public List<Column> getColumns() {
        return columns;
    }

    public boolean isStar() {
        return star;
    }

    public void setStar(boolean star) {
        this.star = star;
    }

    public OrderExpression getOrderExpression() {
        return orderExpression;
    }

    public void setOrderExpression(OrderExpression orderExpression) {
        this.orderExpression = orderExpression;
    }

    public List<Condition> getConditions() {
        return whereExpression.getConditions();
    }

    public List<Keyword> getConnectors() {
        return whereExpression.getConnectors();
    }

    public boolean isMatched(Map<String, String> data) throws SQLParseException {
        return whereExpression.isMatched(data);
    }
}
