package com.sea.lyrad.exec;

import com.sea.lyrad.parse.SQLParseException;
import com.sea.lyrad.parse.stmt.SQLStatement;
import com.sea.lyrad.parse.stmt.dal.DALStatement;
import com.sea.lyrad.parse.stmt.dcl.DCLStatement;
import com.sea.lyrad.parse.stmt.ddl.DDLStatement;
import com.sea.lyrad.parse.stmt.dml.DMLStatement;
import com.sea.lyrad.parse.stmt.dql.DQLStatement;

public class SQLExecutor {

    public String execute(User user, SQLStatement statement) throws DBProcessException, SQLParseException {
        if (user.getCurrentDB() != null) {
            DBManager dbManager = DBManager.getInstance();
            user.setCurrentDB(dbManager.getDatabase(user.getCurrentDB().getName()));//刷新
        }
        if (statement instanceof DALStatement) {
            return new DALExecutor().execute(user, (DALStatement) statement);
        } else if (statement instanceof DDLStatement) {
            return new DDLExecutor().execute(user, (DDLStatement) statement);
        } else if (statement instanceof DCLStatement) {
            return new DCLExecutor().execute(user, (DCLStatement) statement);
        } else if (statement instanceof DMLStatement) {
            return new DMLExecutor().execute(user, (DMLStatement) statement);
        } else if (statement instanceof DQLStatement) {
            return new DQLExecutor().execute(user, (DQLStatement) statement);
        }
        throw new DBProcessException("Unsupported SQL statement.");
    }
}
