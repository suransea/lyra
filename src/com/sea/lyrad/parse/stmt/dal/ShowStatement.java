package com.sea.lyrad.parse.stmt.dal;

import com.sea.lyrad.lex.token.Keyword;

public class ShowStatement extends DALStatement {

    public Keyword getItem() {
        return item;
    }

    public void setItem(Keyword item) {
        this.item = item;
    }

    private Keyword item;
}
