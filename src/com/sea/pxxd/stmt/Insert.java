package com.sea.pxxd.stmt;

import com.sea.pxxd.DBManager;
import com.sea.pxxd.DBProcessException;
import com.sea.pxxd.User;
import com.sea.pxxd.db.Database;
import com.sea.pxxd.db.Table;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.tree.DefaultElement;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;

public class Insert implements Statement {

    private String sql;
    private String tableName;
    private List<List<String>> values;

    public Insert(Matcher matcher) {
        sql = matcher.group(0);
        tableName = matcher.group(1);
        values = valueSplit(matcher.group(2));
    }

    private List<List<String>> valueSplit(String values) {
        char[] s = values.toCharArray();
        List<List<String>> result = new ArrayList<>();
        List<String> subValue = new ArrayList<>();
        StringBuilder value = new StringBuilder();
        boolean start = false;
        boolean stringStart = false;
        boolean ignore = false;
        for (int i = 0; i < s.length; i++) {
            char c = s[i];
            if (c == '\'') {
                stringStart = !stringStart;
                value.append(c);
                continue;
            }
            if (stringStart) {
                value.append(c);
                continue;
            }
            if (c == ' ') continue;
            if (ignore) {
                ignore = false;
                continue;
            }
            if (c == ',') {
                subValue.add(value.toString());
                value = new StringBuilder();
                continue;
            }
            if (c == '(') {
                start = true;
                continue;
            }
            if (c == ')') {
                subValue.add(value.toString());
                value = new StringBuilder();
                result.add(subValue);
                subValue = new ArrayList<>();
                start = false;
                ignore = true;
                continue;
            }
            if (start) value.append(c);
        }
        return result;
    }

    @Override
    public String toString() {
        return sql;
    }

    @Override
    public String execute(User user) throws DBProcessException {
        Database database = user.getCurrentDB();
        if (database == null) {
            throw new DBProcessException("Please use a database firstly.");
        }
        Document document = database.getDocument();
        Element tableElement = null;
        for (Iterator<Element> it = document.getRootElement().elementIterator("table"); it.hasNext(); ) {
            Element element = it.next();
            if (element.attributeValue("name").equals(tableName)) {
                tableElement = element;
                break;
            }
        }
        if (tableElement == null) {
            throw new DBProcessException("The target table is not exist.");
        }
        List<Element> elements = new ArrayList<>();
        for (List<String> value : values) {
            if (tableElement.elements("attr").size() != value.size()) {
                throw new DBProcessException("The values count is not matching.");
            }
            Table table = database.getTable(tableName);
            Element element = new DefaultElement("data");
            for (int i = 0; i < table.getAttributes().size(); i++) {
                Table.Attribute attribute = table.getAttributes().get(i);
                if (attribute.getType() == Table.Attribute.Type.INT) {
                    try {
                        int number = Integer.parseInt(value.get(i));
                        element.addAttribute(attribute.getName(), Integer.toString(number));
                    } catch (NumberFormatException e) {
                        throw new DBProcessException("The format of value is not right.");
                    }
                } else if (attribute.getType() == Table.Attribute.Type.VARCHAR) {
                    String string = value.get(i);
                    if (string.length() > attribute.getLength()) {
                        throw new DBProcessException("The length is outsize.");
                    }
                    if (!(string.startsWith("'") && string.endsWith("'"))) {
                        throw new DBProcessException("Please wrap the string with '' .");
                    }
                    element.addAttribute(attribute.getName(), string.substring(1, string.length() - 1));
                }
            }
            elements.add(element);
        }
        elements.forEach(tableElement::add);
        DBManager dbManager = new DBManager();
        dbManager.write(database);
        return "Inserted.";
    }
}
