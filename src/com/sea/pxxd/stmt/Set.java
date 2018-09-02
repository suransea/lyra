package com.sea.pxxd.stmt;

import com.sea.pxxd.DBManager;
import com.sea.pxxd.DBProcessException;
import com.sea.pxxd.User;
import com.sea.pxxd.db.Database;
import com.sea.pxxd.util.AESCrypto;
import org.dom4j.Element;

import java.util.Iterator;
import java.util.regex.Matcher;

public class Set implements Statement {

    private String sql;
    private String username;
    private String password;

    public Set(Matcher matcher) {
        sql = matcher.group(0);
        username = matcher.group(1);
        password = matcher.group(2);
    }

    @Override
    public String toString() {
        return sql;
    }

    @Override
    public String execute(User user) throws DBProcessException {
        if (!user.getName().equals("root") && !user.getName().equals(username)) {
            throw new DBProcessException("Permission denied.");
        }
        DBManager dbManager = new DBManager();
        Database userDB = dbManager.getDatabase("pxx");
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
            if (element.attributeValue("username").equals(username)) {
                AESCrypto aes = new AESCrypto("5494");
                element.attribute("password").setValue(aes.encode(password));
                dbManager.write(userDB);
                return "Password changed.";
            }
        }
        throw new DBProcessException("The target user is not exist.");
    }
}
