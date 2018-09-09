package com.sea.lyrad.exec;

import com.sea.lyrad.db.Database;
import com.sea.lyrad.lex.token.Keyword;
import com.sea.lyrad.parse.stmt.context.Column;
import com.sea.lyrad.parse.stmt.ddl.CreateStatement;
import com.sea.lyrad.parse.stmt.ddl.DDLStatement;
import com.sea.lyrad.parse.stmt.ddl.DropStatement;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.util.Iterator;
import java.util.List;

public class DDLExecutor extends SQLExecutor {
    private User user;
    private DDLStatement statement;

    public String execute(User user, DDLStatement statement) throws DBProcessException {
        this.user = user;
        this.statement = statement;
        if (statement instanceof CreateStatement) {
            return executeCreate();
        } else if (statement instanceof DropStatement) {
            return executeDrop();
        }
        throw new DBProcessException("Unsupported DDL statement.");
    }

    private String executeCreate() throws DBProcessException {
        CreateStatement stmt = (CreateStatement) statement;
        if (stmt.getItem() == Keyword.DATABASE) {
            DBManager dbManager = new DBManager();
            List<String> fileNames = dbManager.getDBNames();
            if (fileNames.contains(stmt.getDBName())) {
                throw new DBProcessException("The database is already exist.");
            }
            Document document = DocumentHelper.createDocument();
            Element root = document.addElement("database");
            root.addAttribute("name", stmt.getDBName());
            Database database = new Database(stmt.getDBName(), document);
            dbManager.write(database);
            return String.format("Database %s created.", stmt.getDBName());
        } else if (stmt.getItem() == Keyword.TABLE) {
            Database database = user.getCurrentDB();
            if (database == null) {
                throw new DBProcessException("Please select a database firstly.");
            }
            if (database.getTable(stmt.getTableName()) != null) {
                throw new DBProcessException("The table name is already exist.");
            }
            Document document = database.getDocument();
            Element root = document.getRootElement();
            Element tableElement = root.addElement("table");
            tableElement.addAttribute("name", stmt.getTableName());
            for (Column column : stmt.getColumns()) {
                Element attrElement = tableElement.addElement("attr");
                attrElement.addAttribute("name", column.getColumnName());
                attrElement.addAttribute("type", column.getType().name().toLowerCase());
                if (column.getTypeLength() != -1) {
                    attrElement.addAttribute("length", Integer.toString(column.getTypeLength()));
                }
            }
            DBManager dbManager = new DBManager();
            dbManager.write(database);
            user.setCurrentDB(dbManager.getDatabase(user.getCurrentDB().getName()));//刷新
            return String.format("Table %s created.", stmt.getTableName());
        }
        throw new DBProcessException("Unknown error.");
    }

    private String executeDrop() throws DBProcessException {
        DropStatement stmt = (DropStatement) statement;
        if (stmt.getItem() == Keyword.DATABASE) {
            if (!user.getAccessDBNames().contains(stmt.getDBName())) {
                throw new DBProcessException("The target database is not exist.");
            }
            DBManager dbManager = new DBManager();
            dbManager.deleteDatabase(stmt.getDBName());
            if (user.getCurrentDB().getName().equals(stmt.getDBName())) {
                user.setCurrentDB(null);
            }
            return String.format("Database %s deleted.", stmt.getDBName());
        } else if (stmt.getItem() == Keyword.TABLE) {
            if (user.getCurrentDB() == null) {
                throw new DBProcessException("Please select a database firstly.");
            }
            Element rootElement = user.getCurrentDB().getDocument().getRootElement();
            for (Iterator<Element> it = rootElement.elementIterator("table"); it.hasNext(); ) {
                Element element = it.next();
                if (element.attributeValue("name").equals(stmt.getTableName())) {
                    element.detach();
                    DBManager dbManager = new DBManager();
                    dbManager.write(user.getCurrentDB());
                    return String.format("Table %s deleted.", stmt.getTableName());
                }
            }
            throw new DBProcessException("The target table is not exist.");
        }
        throw new DBProcessException("Unknown error.");
    }
}
