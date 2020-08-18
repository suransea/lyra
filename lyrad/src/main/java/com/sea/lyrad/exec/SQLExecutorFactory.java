package com.sea.lyrad.exec;

import com.sea.lyrad.stmt.SQLStatement;
import com.sea.lyrad.stmt.dal.DALStatement;
import com.sea.lyrad.stmt.dcl.DCLStatement;
import com.sea.lyrad.stmt.ddl.DDLStatement;
import com.sea.lyrad.stmt.dml.DMLStatement;
import com.sea.lyrad.stmt.dql.DQLStatement;

/**
 * SQL 执行器工厂
 */
public class SQLExecutorFactory {
    public SQLExecutorFactory() {
    }

    /**
     * 根据传入的statement创建对应的SQL执行器对象
     */
    public SQLExecutor createInstance(SQLStatement statement) throws DBProcessException {
        if (statement instanceof DALStatement) {
            return new DALExecutor();
        } else if (statement instanceof DDLStatement) {
            return new DDLExecutor();
        } else if (statement instanceof DCLStatement) {
            return new DCLExecutor();
        } else if (statement instanceof DMLStatement) {
            return new DMLExecutor();
        } else if (statement instanceof DQLStatement) {
            return new DQLExecutor();
        }
        throw new DBProcessException("Unsupported SQL statement.");
    }
}
