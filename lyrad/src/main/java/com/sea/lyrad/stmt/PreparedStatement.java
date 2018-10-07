package com.sea.lyrad.stmt;

import com.sea.lyrad.parse.SQLParseException;
import com.sea.lyrad.stmt.common.Column;
import com.sea.lyrad.stmt.common.Condition;
import com.sea.lyrad.stmt.dml.InsertStatement;
import com.sea.lyrad.stmt.dml.UpdateStatement;
import com.sea.lyrad.stmt.dql.SelectStatement;
import org.json.JSONArray;

import java.util.List;

/**
 * 编译后的sql语句
 */
public class PreparedStatement {
    private SQLStatement statement;

    public PreparedStatement(SQLStatement statement) {
        this.statement = statement;
    }

    /**
     * 填充参数以转换成标准的sql语句对象
     *
     * @param params 待填充的参数
     * @return 标准sql语句对象
     * @throws SQLParseException 参数数量不匹配
     */
    public SQLStatement toSQLStatement(JSONArray params) throws SQLParseException {
        if (statement instanceof SelectStatement) {
            SelectStatement stmt = (SelectStatement) statement;
            if (params.length() != stmt.getConditions().size()) {
                throw new SQLParseException("The prepared value count is not matched.");
            }
            int index = 0;
            for (Condition condition : stmt.getConditions()) {
                condition.setValue(params.get(index++).toString());
            }
        } else if (statement instanceof UpdateStatement) {
            UpdateStatement stmt = (UpdateStatement) statement;
            if (params.length() != stmt.getColumns().size() + stmt.getConditions().size()) {
                throw new SQLParseException("The prepared value count is not matched.");
            }
            int index = 0;
            for (Column column : stmt.getColumns()) {
                column.setValue(params.get(index++).toString());
            }
            for (Condition condition : stmt.getConditions()) {
                condition.setValue(params.get(index++).toString());
            }
        } else if (statement instanceof InsertStatement) {
            InsertStatement stmt = (InsertStatement) statement;
            if (params.length() != stmt.getValues().size() * stmt.getValues().get(0).size()) {
                throw new SQLParseException("The prepared value count is not matched.");
            }
            int index = 0;
            for (List<String> entry : stmt.getValues()) {
                for (int i = 0; i < entry.size(); i++) {
                    entry.set(i, params.get(index++).toString());
                }
            }
        }
        return statement;
    }
}
