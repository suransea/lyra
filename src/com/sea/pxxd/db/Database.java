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

    public Document getDocment() {
        return document;
    }

    private Document document;

    public Database(String name, Document document) {
        this.name = name;
        this.document = document;
        tables = new ArrayList<>();
    }
}
