package com.sea.pxxd.db;

import com.sea.pxxd.DBProcessException;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;

import java.util.*;

public class Database {
    public String getName() {
        return name;
    }

    private String name;

    public List<Table> getTables() {
        return tables;
    }

    private List<Table> tables;

    public Document getDocument() {
        return document;
    }

    private Document document;

    public Table getTable(String name) {
        for (Table table : tables) {
            if (table.getName().equals(name)) {
                return table;
            }
        }
        return null;
    }

    public List<Map<String, String>> getRows(String tableName) throws DBProcessException {
        List<Map<String, String>> result = new ArrayList<>();
        Element rootElement = document.getRootElement();
        Element tableElement = null;
        for (Iterator<Element> it = rootElement.elementIterator("table"); it.hasNext(); ) {
            Element element = it.next();
            if (element.attributeValue("name").equals(tableName)) {
                tableElement = element;
                break;
            }
        }
        if (tableElement == null) {
            throw new DBProcessException("The target table is not exist.");
        }
        for (Iterator<Element> it = tableElement.elementIterator("data"); it.hasNext(); ) {
            Map<String, String> values = new HashMap<>();
            for (Iterator<Attribute> attrIt = it.next().attributeIterator(); attrIt.hasNext(); ) {
                Attribute attribute = attrIt.next();
                values.put(attribute.getName(), attribute.getValue());
            }
            result.add(values);
        }
        return result;
    }

    public Database(String name, Document document) {
        this.name = name;
        this.document = document;
        tables = new ArrayList<>();
    }
}
