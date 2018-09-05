package com.sea.lyrad.stmt;

import com.sea.lyrad.DBManager;
import com.sea.lyrad.DBProcessException;
import com.sea.lyrad.User;
import com.sea.lyrad.db.Database;
import com.sea.lyrad.db.Table;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;

public class Delete implements Statement {

    private String sql;
    private String tableName;
    private String left;
    private String compare;
    private String right;

    public Delete(Matcher matcher) {
        sql = matcher.group(0);
        tableName = matcher.group(1);
        left = matcher.group(2);
        compare = matcher.group(3);
        right = matcher.group(4);
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
        Element rootElement = database.getDocument().getRootElement();
        Element tableElement = null;
        for (Iterator<Element> it = rootElement.elementIterator("table"); it.hasNext(); ) {
            Element element = it.next();
            if (element.attributeValue("name").equals(tableName)) {
                tableElement = element;
                break;
            }
        }
        int count = 0;
        if (left == null) {
            for (Iterator<Element> it = tableElement.elementIterator("data"); it.hasNext(); ) {
                it.next().detach();
                count++;
            }
        } else {
            for (Iterator<Element> it = tableElement.elementIterator("data"); it.hasNext(); ) {
                Element element = it.next();
                switch (compare) {
                    case "=":
                        if (element.attributeValue(left).compareTo(right) == 0) {
                            element.detach();
                            count++;
                        }
                        break;
                    case "<":
                        if (element.attributeValue(left).compareTo(right) < 0) {
                            element.detach();
                            count++;
                        }
                        break;
                    case ">":
                        if (element.attributeValue(left).compareTo(right) > 0) {
                            element.detach();
                            count++;
                        }
                        break;
                    case ">=":
                        if (element.attributeValue(left).compareTo(right) >= 0) {
                            element.detach();
                            count++;
                        }
                        break;
                    case "<=":
                        if (element.attributeValue(left).compareTo(right) <= 0) {
                            element.detach();
                            count++;
                        }
                        break;
                    case "<>":
                        if (element.attributeValue(left).compareTo(right) != 0) {
                            element.detach();
                            count++;
                        }
                        break;
                }
            }
        }
        DBManager dbManager = new DBManager();
        dbManager.write(database);
        return String.format("%d item(s) deleted.", count);
    }
}
