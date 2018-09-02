package com.sea.pxxd.db;

import org.dom4j.Document;

import java.util.ArrayList;
import java.util.List;

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

    public Database(String name, Document document) {
        this.name = name;
        this.document = document;
        tables = new ArrayList<>();
    }
}
