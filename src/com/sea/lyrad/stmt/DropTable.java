package com.sea.lyrad.stmt;

import com.sea.lyrad.DBManager;
import com.sea.lyrad.DBProcessException;
import com.sea.lyrad.User;
import org.dom4j.Element;

import java.util.Iterator;
import java.util.regex.Matcher;

public class DropTable implements Statement {

    private String sql;
    private String tableName;

    public DropTable(Matcher matcher) {
        sql = matcher.group(0);
        tableName = matcher.group(1);
    }

    @Override
    public String toString() {
        return sql;
    }

    @Override
    public String execute(User user) throws DBProcessException {
        if (user.getCurrentDB() == null) {
            throw new DBProcessException("Please select a database firstly.");
        }
        Element rootElement = user.getCurrentDB().getDocument().getRootElement();
        for (Iterator<Element> it = rootElement.elementIterator("table"); it.hasNext(); ) {
            Element element = it.next();
            if (element.attributeValue("name").equals(tableName)) {
                element.detach();
                DBManager dbManager = new DBManager();
                dbManager.write(user.getCurrentDB());
                return "Deleted.";
            }
        }
        throw new DBProcessException("The target table is not exist.");
    }
}
