package com.sea.pxxd.stmt;

import com.sea.pxxd.DBManager;
import com.sea.pxxd.DBProcessException;
import com.sea.pxxd.User;
import com.sea.pxxd.db.Database;
import com.sea.pxxd.db.Table;
import org.dom4j.Element;
import org.dom4j.dom.DOMElement;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;

public class Update implements Statement {

    private String sql;
    private String tableName;
    private String attr;
    private String newValue;
    private String left;
    private String compare;
    private String right;

    public Update(Matcher matcher) {
        sql = matcher.group(0);
        tableName = matcher.group(1);
        attr = matcher.group(2);
        newValue = matcher.group(3);
        left = matcher.group(4);
        compare = matcher.group(5);
        right = matcher.group(6);
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
        if (!allAttrs.contains(attr)) {
            throw new DBProcessException("The target column is not exist.");
        }
        if (!allAttrs.contains(left)) {
            throw new DBProcessException("The where expression left is not exist.");
        }
        Table.Attribute attribute = table.getAttribute(attr);
        if (attribute.getType() == Table.Attribute.Type.VARCHAR) {
            if (newValue.length() > attribute.getLength()) {
                throw new DBProcessException("The new value length is outsize.");
            }
        } else if (attribute.getType() == Table.Attribute.Type.INT) {
            try {
                Integer.parseInt(newValue);
            } catch (NumberFormatException e) {
                throw new DBProcessException("The format of new value is not right.");
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
        for (Iterator<Element> it = tableElement.elementIterator("data"); it.hasNext(); ) {
            Element element = it.next();
            switch (compare) {
                case "=":
                    if (element.attributeValue(left).compareTo(right) == 0) {
                        element.setAttributeValue(attr, newValue);
                        count++;
                    }
                    break;
                case "<":
                    if (element.attributeValue(left).compareTo(right) < 0) {
                        element.setAttributeValue(attr, newValue);
                        count++;
                    }
                    break;
                case ">":
                    if (element.attributeValue(left).compareTo(right) > 0) {
                        element.setAttributeValue(attr, newValue);
                        count++;
                    }
                    break;
                case ">=":
                    if (element.attributeValue(left).compareTo(right) >= 0) {
                        element.setAttributeValue(attr, newValue);
                        count++;
                    }
                    break;
                case "<=":
                    if (element.attributeValue(left).compareTo(right) <= 0) {
                        element.setAttributeValue(attr, newValue);
                        count++;
                    }
                    break;
                case "<>":
                    if (element.attributeValue(left).compareTo(right) != 0) {
                        element.setAttributeValue(attr, newValue);
                        count++;
                    }
                    break;
            }
        }
        DBManager dbManager = new DBManager();
        dbManager.write(database);
        return String.format("%d item(s) changed.", count);
    }
}
