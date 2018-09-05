package com.sea.lyrad.stmt;

import com.sea.lyrad.DBProcessException;
import com.sea.lyrad.User;
import com.sea.lyrad.db.Database;
import com.sea.lyrad.db.Table;
import com.sea.lyrad.util.ConsoleTable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

public class Select implements Statement {

    private String sql;
    private List<String> attrs;
    private String tableName;
    private String left;
    private String compare;
    private String right;

    public Select(Matcher matcher) {
        sql = matcher.group(0);
        attrs = new ArrayList<>();
        if (matcher.group(1).contains(",")) {
            for (String s : matcher.group(1).split(",")) {
                attrs.add(s.trim());
            }
        } else {
            attrs.add(matcher.group(1).trim());
        }
        tableName = matcher.group(2);
        left = matcher.group(3);
        compare = matcher.group(4);
        right = matcher.group(5);
    }

    @Override
    public String toString() {
        return sql;
    }

    @Override
    public String execute(User user) throws DBProcessException {
        Database database = user.getCurrentDB();
        if (database == null) {
            throw new DBProcessException("Please select a database firstly.");
        }
        List<Map<String, String>> datas = database.getRows(tableName);
        Table table = database.getTable(tableName);
        if (table == null) {
            throw new DBProcessException("The target table is not exist.");
        }
        List<String> allAttrs = new ArrayList<>();
        for (Table.Attribute attr : table.getAttributes()) {
            allAttrs.add(attr.getName());
        }
        if (left != null) {
            if (!allAttrs.contains(left)) {
                throw new DBProcessException("The where expression left is not exist.");
            }
        }
        if (attrs.get(0).equals("*")) {
            attrs = allAttrs;
        }
        for (String attr : attrs) {
            if (table.getAttribute(attr) == null) {
                throw new DBProcessException("Cannot find the column.");
            }
        }
        ConsoleTable consoleTable = new ConsoleTable(attrs.size());
        consoleTable.appendRow();
        for (String attr : attrs) {
            consoleTable.appendColumn(attr);
        }
        for (Map<String, String> entry : datas) {
            boolean flag = false;
            if (left != null) {
                switch (compare) {
                    case ">":
                        if (entry.get(left).compareTo(right) > 0) {
                            flag = true;
                        }
                        break;
                    case "<":
                        if (entry.get(left).compareTo(right) < 0) {
                            flag = true;
                        }
                        break;
                    case "=":
                        if (entry.get(left).compareTo(right) == 0) {
                            flag = true;
                        }
                        break;
                    case ">=":
                        if (entry.get(left).compareTo(right) >= 0) {
                            flag = true;
                        }
                        break;
                    case "<=":
                        if (entry.get(left).compareTo(right) <= 0) {
                            flag = true;
                        }
                        break;
                    case "<>":
                        if (entry.get(left).compareTo(right) != 0) {
                            flag = true;
                        }
                        break;
                }
            } else {
                flag = true;
            }
            if (flag) {
                consoleTable.appendRow();
                for (String attr : attrs) {
                    consoleTable.appendColumn(entry.get(attr));
                }
            }
        }
        return consoleTable.toString();
    }
}
