package com.sea.pxxd;

import com.sea.pxxd.db.Database;
import com.sea.pxxd.db.Table;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DBManager {

    public static final String PATH = "./database";

    public DBManager() {

    }

    public List<String> getDbNames() {
        File database = new File(PATH);
        List<String> fileNames = new ArrayList<>();
        File[] files = database.listFiles();
        if (files == null) {
            return fileNames;
        }
        for (File file : files) {
            String fileName = file.getName();
            if (fileName.endsWith(".xml")) {
                fileNames.add(fileName.substring(0, fileName.length() - 4));
            }
        }
        return fileNames;
    }

    public Database getDatabase(String dbName) throws DBProcessException {
        SAXReader reader = new SAXReader();
        Document document;
        try {
            document = reader.read(PATH + "/" + dbName + ".xml");
        } catch (DocumentException e) {
            throw new DBProcessException("Unknown error.");
        }
        Element root = document.getRootElement();
        Database database = new Database(root.attributeValue("name"), document);
        Iterator<Element> it = root.elementIterator("table");
        while (it.hasNext()) {
            Element element = it.next();
            Table table = new Table(element.attributeValue("name"));
            Iterator<Element> subIt = element.elementIterator("attr");
            while (subIt.hasNext()) {
                Element attr = subIt.next();
                String name = attr.attributeValue("name");
                switch (attr.attributeValue("type")) {
                    case "varchar": {
                        table.getAttributes().add(
                                new Table.Attribute(
                                        name,
                                        Table.Attribute.Type.VARCHAR,
                                        Integer.parseInt(attr.attributeValue("length"))
                                )
                        );
                        break;
                    }
                    case "int": {
                        table.getAttributes().add(
                                new Table.Attribute(
                                        name,
                                        Table.Attribute.Type.INT
                                )
                        );
                        break;
                    }
                }
            }
            database.getTables().add(table);
        }
        return database;
    }
}

