package com.sea.lyrad.exec;

import com.sea.lyrad.db.Database;
import com.sea.lyrad.stmt.SQLStatement;
import com.sea.lyrad.stmt.dcl.AlterUserStatement;
import com.sea.lyrad.stmt.dcl.CreateUserStatement;
import com.sea.lyrad.stmt.dcl.DCLStatement;
import com.sea.lyrad.util.AESCrypto;
import com.sea.lyrad.util.Log;
import com.sea.lyrad.util.XMLUtil;
import org.dom4j.Element;

import java.util.Iterator;

public class DCLExecutor implements SQLExecutor {
    private static final String PASSWORD_REGEX;

    static {
        //验证密码格式的正则表达式
        PASSWORD_REGEX = "^([A-Z]|[a-z]|[0-9]|[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~\uff01@#\uffe5%\u2026\u2026&*\uff08\uff09\u2014\u2014+|{}\u3010\u3011\u2018\uff1b\uff1a\u201d\u201c'\u3002\uff0c\u3001\uff1f]){6,20}$";
    }

    private User user;
    private DCLStatement statement;

    @Override
    public String execute(User user, SQLStatement statement) throws DBProcessException {
        this.user = user;
        this.statement = (DCLStatement) statement;
        if (statement instanceof CreateUserStatement) {
            return executeCreate();
        } else if (statement instanceof AlterUserStatement) {
            return executeAlter();
        }
        throw new DBProcessException("Unsupported DCL statement.");
    }

    private String executeCreate() throws DBProcessException {
        CreateUserStatement stmt = (CreateUserStatement) statement;
        if (!user.getName().equals("root")) {
            throw new DBProcessException("Permission denied.");
        }
        DBManager dbManager = DBManager.getInstance();
        Database userDB = dbManager.getDatabase("lyra");
        Element rootElement = userDB.getDocument().getRootElement();
        Element tableElement = XMLUtil.getTableElement(rootElement, "user");
        if (tableElement == null) {
            String message = "Error: inside table `user` lost";
            Log.a(message);
            throw new DBProcessException(message);
        }
        for (Iterator<Element> it = tableElement.elementIterator("data"); it.hasNext(); ) {
            Element element = it.next();
            if (element.attributeValue("username").equals(stmt.getUsername())) {
                throw new DBProcessException(String.format("The target user %s is already exist.", stmt.getUsername()));
            }
        }
        AESCrypto aes = new AESCrypto("5494");
        String password = stmt.getPassword();
        if (!password.matches(PASSWORD_REGEX)) {
            throw new DBProcessException("The password is illegal.");
        }
        Element element = tableElement.addElement("data");
        element.addAttribute("username", stmt.getUsername());
        element.addAttribute("passwd", aes.encode(password));
        dbManager.write(userDB);
        return String.format("User %s added.", stmt.getUsername());
    }

    private String executeAlter() throws DBProcessException {
        AlterUserStatement stmt = (AlterUserStatement) statement;
        if (!user.getName().equals("root") && !user.getName().equals(stmt.getUsername())) {
            throw new DBProcessException("Permission denied.");
        }
        DBManager dbManager = DBManager.getInstance();
        Database userDB = dbManager.getDatabase("lyra");
        Element rootElement = userDB.getDocument().getRootElement();
        Element tableElement = XMLUtil.getTableElement(rootElement, "user");
        if (tableElement == null) {
            String message = "Error: inside table `user` lost";
            Log.a(message);
            throw new DBProcessException(message);
        }
        for (Iterator<Element> it = tableElement.elementIterator("data"); it.hasNext(); ) {
            Element element = it.next();
            if (element.attributeValue("username").equals(stmt.getUsername())) {
                AESCrypto aes = new AESCrypto("5494");
                String password = stmt.getPassword();
                if (!password.matches(PASSWORD_REGEX)) {
                    throw new DBProcessException("The password is illegal.");
                }
                element.attribute("passwd").setValue(aes.encode(password));
                dbManager.write(userDB);
                return String.format("Password changed for user %s.", stmt.getUsername());
            }
        }
        throw new DBProcessException(String.format("The target user %s is not exist.", stmt.getUsername()));
    }
}
