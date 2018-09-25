package com.sea.lyrad.exec;

import com.sea.lyrad.db.Database;
import com.sea.lyrad.db.DatabasePool;
import com.sea.lyrad.db.table.Attribute;
import com.sea.lyrad.db.table.Table;
import com.sea.lyrad.util.AESCrypto;
import com.sea.lyrad.util.Log;
import com.sea.lyrad.util.XMLUtil;
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

    private DatabasePool databasePool;

    private DBManager() {
        databasePool = new DatabasePool();
    }

    public static DBManager getInstance() {
        if (dbManager == null) {
            dbManager = new DBManager();
        }
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

    public Database getDatabase(String name) throws DBProcessException {
        return databasePool.getDatabase(name);
    }

    public Database takeDatabase(String dbName) throws DBProcessException {
        SAXReader reader = new SAXReader();
        Document document;
        try {
            InputStream inputStream = new FileInputStream(PATH + "/" + dbName + ".xml");
            InputStreamReader streamReader = new InputStreamReader(inputStream, "utf-8");
            document = reader.read(streamReader);
        } catch (DocumentException | FileNotFoundException | UnsupportedEncodingException e) {
            Log.a(e.getMessage());
            throw new DBProcessException("Unknown error.");
        }
        Element rootElement = document.getRootElement();
        Database database = new Database(rootElement.attributeValue("name"), document);
        for (Iterator<Element> it = rootElement.elementIterator("table"); it.hasNext(); ) {
            Element tableElement = it.next();
            Table table = new Table(tableElement.attributeValue("name"));
            for (Iterator<Element> subIt = tableElement.elementIterator("attr"); subIt.hasNext(); ) {
                Element attributeElement = subIt.next();
                String name = attributeElement.attributeValue("name");
                String type = attributeElement.attributeValue("type");
                String length = attributeElement.attributeValue("length");
                Attribute attribute = new Attribute(name, type, length);
                table.addAttribute(attribute);
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
        Element tableElement = XMLUtil.getTableElement(rootElement, "user");
        if (tableElement == null) {
            String message = "Error: inside table `user` lost";
            Log.a(message);
            throw new DBProcessException(message);
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

