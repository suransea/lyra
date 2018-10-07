package com.sea.lyrad.stmt.common;

import com.sea.lyrad.lex.token.Symbol;
import com.sea.lyrad.parse.SQLParseException;

public class Condition {
    private Column column;
    private Symbol operator;
    private String value;

    public boolean isMatched(String value) throws SQLParseException {
        double outcome;
        try {
            double left = Double.parseDouble(value);
            double right = Double.parseDouble(this.value);
            outcome = left - right;
        } catch (NumberFormatException e) {
            outcome = value.compareTo(this.value);
        }
        switch (operator) {
            case EQ:
                if (value.matches(this.value)) {
                    return true;
                }
                return outcome == 0;
            case GT:
                return outcome > 0;
            case LT:
                return outcome < 0;
            case GT_EQ:
                return outcome >= 0;
            case LT_EQ:
                return outcome <= 0;
            case LT_GT:
                return outcome != 0;
            case BANG_EQ:
                return outcome != 0;
            default:
                throw new SQLParseException("Unknown condition symbol '%s'.", operator.getLiterals());
        }
    }

    public Column getColumn() {
        return column;
    }

    public void setColumn(Column column) {
        this.column = column;
    }

    public Symbol getOperator() {
        return operator;
    }

    public void setOperator(Symbol operator) {
        this.operator = operator;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
