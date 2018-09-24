package com.sea.lyrad.exec;

import com.sea.lyrad.db.Database;
import com.sea.lyrad.db.Table;
import com.sea.lyrad.util.AESCrypto;
import com.sea.lyrad.util.Log;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DBManager {

    private static final String PATH = "./database";
    private static DBManager dbManager = null;

    private DBManager() {
    }

    public static DBManager getInstance() {
        if (dbManager == null) dbManager = new DBManager();
        return dbManager;
    }

    public List<String> getDBNames() {
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

    public void deleteDatabase(String name) throws DBProcessException {
        File file = new File(PATH + "/" + name + ".xml");
        if (!file.delete()) {
            throw new DBProcessException("The target database is not exist.");
        }
    }

    public void write(Database database) throws DBProcessException {
        Document document = database.getDocument();
        try {
            OutputStream outputStream = new FileOutputStream(DBManager.PATH + "/" + database.getName() + ".xml");
            OutputStreamWriter writer = new OutputStreamWriter(outputStream, "utf-8");
            document.write(writer);
            writer.close();
            outputStream.close();
        } catch (IOException e) {
            Log.a(e.getMessage());
            throw new DBProcessException("IO error.");
        }
    }

    public Database getDatabase(String dbName) throws DBProcessException {
        SAXReader reader = new SAXReader();
        Document document;
        try {
            InputStream inputStream = new FileInputStream(PATH + "/" + dbName + ".xml");
            InputStreamReader streamReader = new InputStreamReader(inputStream, "utf-8");
            document = reader.read(streamReader);
        } catch (DocumentException | FileNotFoundException | UnsupportedEncodingException e) {
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

    public boolean verify(String name, String password) throws DBProcessException {
        AESCrypto aes = new AESCrypto("5494");
        String encodePassword = aes.encode(password);
        Database userDB = getDatabase("lyra");
        Element rootElement = userDB.getDocument().getRootElement();
        Element tableElement = null;
        for (Iterator<Element> it = rootElement.elementIterator("table"); it.hasNext(); ) {
            Element element = it.next();
            if (element.attributeValue("name").equals("user")) {
                tableElement = element;
                break;
            }
        }
        for (Iterator<Element> it = tableElement.elementIterator("data"); it.hasNext(); ) {
            Element element = it.next();
            if (element.attributeValue("username").equals(name) &&
                    element.attributeValue("passwd").equals(encodePassword)) {
                return true;
            }
        }
        return false;
    }
}

