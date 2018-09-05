package com.sea.lyrad.stmt;

import com.sea.lyrad.DBManager;
import com.sea.lyrad.DBProcessException;
import com.sea.lyrad.User;
import com.sea.lyrad.db.Database;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

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
    public String execute(User user) throws DBProcessException {
        DBManager dbManager = new DBManager();
        List<String> fileNames = dbManager.getDBNames();
        if (fileNames.contains(dbName)) {
            throw new DBProcessException("The database is already exist.");
        }
        Document document = DocumentHelper.createDocument();
        Element root = document.addElement("database");
        root.addAttribute("name", dbName);
        Database database = new Database(dbName, document);
        dbManager.write(database);
        return "Created.";
    }

    @Override
    public String toString() {
        return sql;
    }
}
