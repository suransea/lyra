package com.sea.lyrad.stmt.dql;

import com.sea.lyrad.stmt.BaseSQLStatement;
import com.sea.lyrad.stmt.SQLStatement;

public class DQLStatement extends BaseSQLStatement implements SQLStatement {
    public DQLStatement(String sql) {
        super(sql);
    }
}
