package com.sea.lyrad.stmt.common;

import com.sea.lyrad.lex.token.Keyword;

import java.util.ArrayList;
import java.util.List;

/**
 * SQL语句的列
 */
public class Column {
    private String columnName;
    private Keyword type;
    private int typeLength;
    private String value;
    private List<Constraint> constraints;

    public Column() {
        constraints = new ArrayList<>();
        typeLength = -1;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public Keyword getType() {
        return type;
    }

    public void setType(Keyword type) {
        this.type = type;
    }

    public int getTypeLength() {
        return typeLength;
    }

    public void setTypeLength(int typeLength) {
        this.typeLength = typeLength;
    }

    public List<Constraint> getConstraints() {
        return constraints;
    }
}
