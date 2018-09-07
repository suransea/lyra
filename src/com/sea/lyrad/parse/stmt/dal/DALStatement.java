package com.sea.lyrad.parse.stmt.dal;

import com.sea.lyrad.lex.token.Keyword;
import com.sea.lyrad.parse.stmt.SQLStatement;

public class DALStatement extends SQLStatement {
    public DALStatement(String sql) {
        super(sql);
    }

    public Keyword getItem() {

        return item;
    }

    public void setItem(Keyword item) {
        this.item = item;
    }

    private Keyword item;
}
