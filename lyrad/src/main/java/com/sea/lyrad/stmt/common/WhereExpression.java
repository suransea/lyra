package com.sea.lyrad.stmt.common;

import com.sea.lyrad.lex.token.Keyword;
import com.sea.lyrad.parse.SQLParseException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WhereExpression {
    private List<Condition> conditions;
    private List<Keyword> connectors;

    public WhereExpression() {
        this.conditions = new ArrayList<>();
        this.connectors = new ArrayList<>();
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
}
