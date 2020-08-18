package com.sea.lyrad.stmt.common;

import com.sea.lyrad.lex.token.Keyword;
import com.sea.lyrad.parse.SQLParseException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * where表达式
 */
public class WhereExpression {
    private List<Condition> conditions;//条件
    private List<Keyword> connectors;  //条件连接词(and/or)

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

    /**
     * 给定数据是否符合该where条件
     *
     * @param data 数据
     * @return true if matched
     * @throws SQLParseException 不支持的条件连接词
     */
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
