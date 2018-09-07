package com.sea.lyrad.exec;

import com.sea.lyrad.parse.stmt.SQLStatement;
import com.sea.lyrad.parse.stmt.dal.DALStatement;

public class SQLExecutor {

    public String execute(User user, SQLStatement statement) throws DBProcessException {
        if (statement instanceof DALStatement) {
            return new DALExecutor().execute(user, (DALStatement) statement);
        }
        throw new DBProcessException("Unsupported SQL statement.");
    }
}
