package com.sea.lyrad.stmt.dal;

import com.sea.lyrad.lex.token.Keyword;
import com.sea.lyrad.stmt.BaseSQLStatement;
import com.sea.lyrad.stmt.SQLStatement;

public class DALStatement extends BaseSQLStatement implements SQLStatement {
    private Keyword item;//value is database or table

    public DALStatement(String sql) {
        super(sql);
    }

    public Keyword getItem() {

        return item;
    }

    public void setItem(Keyword item) {
        this.item = item;
    }
}
