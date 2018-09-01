package com.sea.pxxd.stmt;

import com.sea.pxxd.DBManager;
import com.sea.pxxd.DBProcessException;
import com.sea.pxxd.Log;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;

public class CreateDatabase implements Statement {

    public String getDbName() {
        return dbName;
    }

    private String dbName;
    private String sql;

    public CreateDatabase(Matcher matcher) {
        sql = matcher.group(0);
        dbName = matcher.group(1);
    }

    @Override
    public String execute() throws DBProcessException {
        DBManager dbManager = new DBManager();
        List<String> fileNames = dbManager.getDbNames();
        if (fileNames.contains(dbName)) {
            throw new DBProcessException("The database is already exist.");
        }
        Document document = DocumentHelper.createDocument();
        Element root = document.addElement("database");
        root.addAttribute("name", dbName);
        try {
            FileWriter writer = new FileWriter(DBManager.PATH + "/" + dbName + ".xml");
            document.write(writer);
            writer.close();
        } catch (IOException e) {
            Log.a(e.getMessage());
            throw new DBProcessException("IO error.");
        }
        return "Created.";
    }

    @Override
    public String toString() {
        return sql;
    }
}
