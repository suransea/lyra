package com.sea.lyrad.parse.stmt.context;

import com.sea.lyrad.lex.token.Keyword;

import java.util.ArrayList;
import java.util.Collection;

public class Column {
    private String columnName;
    private Keyword type;
    private int typeLength;
    private Collection<Constraint> constraints;

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

    public Collection<Constraint> getConstraints() {
        return constraints;
    }

    public Column() {
        constraints = new ArrayList<>();
        typeLength = -1;
    }
}
