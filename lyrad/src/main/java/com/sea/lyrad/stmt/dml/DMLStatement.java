package com.sea.lyrad.stmt.dml;

import com.sea.lyrad.lex.token.Keyword;
import com.sea.lyrad.parse.SQLParseException;
import com.sea.lyrad.stmt.BaseSQLStatement;
import com.sea.lyrad.stmt.SQLStatement;
import com.sea.lyrad.stmt.common.Condition;
import com.sea.lyrad.stmt.common.WhereExpression;

import java.util.List;
import java.util.Map;

public class DMLStatement extends BaseSQLStatement implements SQLStatement {
    private String tableName;
    private WhereExpression whereExpression;

    public DMLStatement(String sql) {
        super(sql);
        whereExpression = new WhereExpression();
    }

    public WhereExpression getWhereExpression() {
        return whereExpression;
    }

    public String getTableName() {
        if (tableName.startsWith("`") && tableName.endsWith("`")) {
            return tableName.substring(1, tableName.length() - 1);
        }
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
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
