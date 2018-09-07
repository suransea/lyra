package com.sea.lyrad.exec;

import com.sea.lyrad.parse.stmt.SQLStatement;
import com.sea.lyrad.parse.stmt.dal.DALStatement;
import com.sea.lyrad.parse.stmt.dcl.DCLStatement;
import com.sea.lyrad.parse.stmt.ddl.DDLStatement;
import com.sea.lyrad.parse.stmt.dml.DMLStatement;

public class SQLExecutor {

    public String execute(User user, SQLStatement statement) throws DBProcessException {
        if (statement instanceof DALStatement) {
            return new DALExecutor().execute(user, (DALStatement) statement);
        } else if (statement instanceof DDLStatement) {
            return new DDLExecutor().execute(user, (DDLStatement) statement);
        } else if (statement instanceof DCLStatement) {
            return new DCLExecutor().execute(user, (DCLStatement) statement);
        } else if (statement instanceof DMLStatement) {
            return new DMLExecutor().execute(user, (DMLStatement) statement);
        }
        throw new DBProcessException("Unsupported SQL statement.");
    }
}
