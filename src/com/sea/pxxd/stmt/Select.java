package com.sea.pxxd.stmt;

import com.sea.pxxd.DBProcessException;
import com.sea.pxxd.User;
import com.sea.pxxd.db.Database;
import com.sea.pxxd.util.ConsoleTable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

public class Select implements Statement {

    private String sql;
    private List<String> attr;
    private String tableName;
    private String left;
    private String compare;
    private String right;

    public Select(Matcher matcher) {
        sql = matcher.group(0);
        attr = new ArrayList<>();
        if (matcher.group(1).contains(",")) {
            for (String s : matcher.group(1).split(",")) {
                attr.add(s.trim());
            }
        } else {
            attr.add(matcher.group(1).trim());
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
        List<Map<String,String>> datas=database.getRows(tableName);
        //for()
        //ConsoleTable consoleTable=new ConsoleTable(attr.size());
        return null;
    }
}
