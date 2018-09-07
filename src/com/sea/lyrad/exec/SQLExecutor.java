package com.sea.lyrad.exec;

import com.sea.lyrad.parse.stmt.SQLStatement;
import com.sea.lyrad.parse.stmt.dal.DALStatement;
import com.sea.lyrad.parse.stmt.ddl.DDLStatement;

public class SQLExecutor {

    public String execute(User user, SQLStatement statement) throws DBProcessException {
        if (statement instanceof DALStatement) {
            return new DALExecutor().execute(user, (DALStatement) statement);
        } else if (statement instanceof DDLStatement) {
            return new DDLExecutor().execute(user, (DDLStatement) statement);
        }
        throw new DBProcessException("Unsupported SQL statement.");
    }
}
