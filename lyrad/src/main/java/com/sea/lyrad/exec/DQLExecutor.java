package com.sea.lyrad.exec;

import com.sea.lyrad.db.Database;
import com.sea.lyrad.db.table.Table;
import com.sea.lyrad.db.table.TableAttribute;
import com.sea.lyrad.parse.SQLParseException;
import com.sea.lyrad.stmt.SQLStatement;
import com.sea.lyrad.stmt.common.Column;
import com.sea.lyrad.stmt.common.Condition;
import com.sea.lyrad.stmt.dql.DQLStatement;
import com.sea.lyrad.stmt.dql.SelectStatement;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DQLExecutor implements SQLExecutor {
    private User user;
    private DQLStatement statement;

    @Override
    public String execute(User user, SQLStatement statement) throws DBProcessException, SQLParseException {
        this.user = user;
        this.statement = (DQLStatement) statement;
        if (statement instanceof SelectStatement) {
            return executeSelect();
        }
        throw new DBProcessException("Unsupported DQL statement.");
    }

    private String executeSelect() throws DBProcessException, SQLParseException {
        SelectStatement stmt = (SelectStatement) statement;
        Database database = user.getCurrentDB();
        if (database == null) {
            throw new DBProcessException("Please select a database firstly.");
        }
        Table table = database.getTable(stmt.getTableName());
        if (table == null) {
            throw new DBProcessException("The target table is not exist.");
        }
        List<String> allAttrs = new ArrayList<>();
        for (TableAttribute attr : table.getAttributes()) {
            allAttrs.add(attr.getName());
        }
        boolean selectAllRows = stmt.getConditions().size() == 0;
        boolean selectAllColumns = stmt.isStar();
        boolean order = stmt.getOrderExpression().getColumns().size() > 0;
        boolean asc = stmt.getOrderExpression().isAsc();
        List<String> leftAttrs = new ArrayList<>();
        for (Condition condition : stmt.getConditions()) {
            leftAttrs.add(condition.getColumn().getColumnName());
        }
        List<String> selectAttrs = new ArrayList<>();
        for (Column column : stmt.getColumns()) {
            selectAttrs.add(column.getColumnName());
        }
        List<String> orderAttrs = new ArrayList<>();
        for (Column column : stmt.getOrderExpression().getColumns()) {
            orderAttrs.add(column.getColumnName());
        }
        if (!selectAllRows) {
            if (!allAttrs.containsAll(leftAttrs)) {
                throw new DBProcessException("The one of where expressions left value is not exist.");
            }
        }
        if (!selectAllColumns) {
            if (!allAttrs.containsAll(selectAttrs)) {
                throw new DBProcessException("The select column is not exist.");
            }
        } else {
            selectAttrs = allAttrs;
        }
        if (order) {
            if (!allAttrs.containsAll(orderAttrs)) {
                throw new DBProcessException("The order column is not exist.");
            }
        }
        List<Map<String, String>> data = selectAllRows ?
                database.getRows(table.getName()) :
                database.getRows(table.getName(), stmt.getWhereExpression());
        JSONArray outcome = new JSONArray();
        data.sort(
                (x, y) -> {
                    int result = 0;
                    for (int i = 0; i < orderAttrs.size(); i++) {
                        String left = x.get(orderAttrs.get(i));
                        String right = y.get(orderAttrs.get(i));
                        int subResult;
                        try {
                            double a = Double.parseDouble(left);
                            double b = Double.parseDouble(right);
                            subResult = Double.compare(a, b);
                        } catch (NumberFormatException e) {
                            subResult = left.compareTo(right);
                        }
                        if (subResult != 0 || i == orderAttrs.size() - 1) {
                            result = subResult;
                            break;
                        }
                    }
                    if (!asc) {
                        result = (-result);
                    }
                    return result;
                }
        );
        outcome.put(selectAttrs);
        for (Map<String, String> entry : data) {
            List<String> row = new ArrayList<>();
            for (String column : selectAttrs) {
                row.add(entry.get(column));
            }
            outcome.put(row);
        }
        return outcome.toString();
    }
}
