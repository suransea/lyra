package com.sea.pxxd;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DBManager {

    private static final String PATH = "./database";
    private String name;

    public DBManager(String name) {
        this.name = name;
    }

    private List<String> getDBNames() {
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

    public String CreateDatabase() throws DBProcessException {
        List<String> fileNames = getDBNames();
        if (fileNames.contains(name)) {
            throw new DBProcessException("The database is already exist.");
        }
        Document document = DocumentHelper.createDocument();
        Element root = document.addElement("database");
        root.addAttribute("name", name);
        try {
            FileWriter writer = new FileWriter(PATH + "/" + name + ".xml");
            document.write(writer);
            writer.close();
        } catch (IOException e) {
            Log.a(e.getMessage());
            throw new DBProcessException("IO error.");
        }
        return "Created.";
    }
}
