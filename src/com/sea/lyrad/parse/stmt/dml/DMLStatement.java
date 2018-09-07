package com.sea.lyrad.parse.stmt.dml;

import com.sea.lyrad.lex.token.Keyword;
import com.sea.lyrad.parse.SQLParseException;
import com.sea.lyrad.parse.stmt.SQLStatement;
import com.sea.lyrad.parse.stmt.context.Condition;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DMLStatement extends SQLStatement {
    public DMLStatement(String sql) {
        super(sql);
        conditions = new ArrayList<>();
        connectors = new ArrayList<>();
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<Condition> getConditions() {
        return conditions;
    }

    public List<Keyword> getConnectors() {
        return connectors;
    }

    public boolean isMatched(Map<String, String> data) throws SQLParseException {
        Condition condition = conditions.get(0);
        boolean result = condition.isMatched(data.get(condition.getColumn().getColumnName()));
        for (int i = 0; i < connectors.size(); i++) {
            condition = conditions.get(i + 1);
            if (connectors.get(i) == Keyword.AND) {
                result = result && condition.isMatched(data.get(condition.getColumn().getColumnName()));
            } else if (connectors.get(i) == Keyword.OR) {
                result = result || condition.isMatched(data.get(condition.getColumn().getColumnName()));
            } else {
                throw new SQLParseException("Unsupported connector '%s'.", connectors.get(i).name());
            }
        }
        return result;
    }

    private String tableName;
    private List<Condition> conditions;
    private List<Keyword> connectors;
}
