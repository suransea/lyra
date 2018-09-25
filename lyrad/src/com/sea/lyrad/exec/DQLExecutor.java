package com.sea.lyrad.exec;

import com.sea.lyrad.db.Database;
import com.sea.lyrad.db.table.Table;
import com.sea.lyrad.db.table.TableAttribute;
import com.sea.lyrad.parse.SQLParseException;
import com.sea.lyrad.parse.stmt.context.Column;
import com.sea.lyrad.parse.stmt.context.Condition;
import com.sea.lyrad.parse.stmt.dql.DQLStatement;
import com.sea.lyrad.parse.stmt.dql.SelectStatement;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DQLExecutor extends SQLExecutor {
    private User user;
    private DQLStatement statement;

    public String execute(User user, DQLStatement statement) throws DBProcessException, SQLParseException {
        this.user = user;
        this.statement = statement;
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
        List<Map<String, String>> data = database.getRows(table.getName());
        JSONArray ren = new JSONArray();
        if (!selectAllRows) {
            for (int i = 0; i < data.size(); i++) {
                if (!stmt.isMatched(data.get(i))) {
                    data.remove(i--);
                }
            }
        }
        data.sort(
                (x, y) -> {
                    int result = 0;
                    if (orderAttrs.size() == 1) {
                        result = x.get(orderAttrs.get(0)).compareTo(y.get(orderAttrs.get(0)));
                    }
                    for (int i = 0; i < orderAttrs.size(); i++) {
                        String left = x.get(orderAttrs.get(i));
                        String right = y.get(orderAttrs.get(i));
                        int subResult = left.compareTo(right);
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
        ren.put(selectAttrs);
        for (Map<String, String> entry : data) {
            List<String> row = new ArrayList<>();
            for (String column : selectAttrs) {
                row.add(entry.get(column));
            }
            ren.put(row);
        }
        return ren.toString();
    }
}
