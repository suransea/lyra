package com.sea.lyrad.exec;

import com.sea.lyrad.db.Database;
import com.sea.lyrad.db.table.Table;
import com.sea.lyrad.db.table.TableAttribute;
import com.sea.lyrad.lex.token.Keyword;
import com.sea.lyrad.stmt.SQLStatement;
import com.sea.lyrad.stmt.common.Column;
import com.sea.lyrad.stmt.ddl.CreateStatement;
import com.sea.lyrad.stmt.ddl.DDLStatement;
import com.sea.lyrad.stmt.ddl.DropStatement;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.util.Iterator;
import java.util.List;

public class DDLExecutor implements SQLExecutor {
    private User user;
    private DDLStatement statement;

    @Override
    public String execute(User user, SQLStatement statement) throws DBProcessException {
        this.user = user;
        this.statement = (DDLStatement) statement;
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
            DBManager dbManager = DBManager.getInstance();
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
            Table table = new Table(stmt.getTableName());
            tableElement.addAttribute("name", stmt.getTableName());
            for (Column column : stmt.getColumns()) {
                TableAttribute tableAttribute = new TableAttribute(
                        column.getColumnName(),
                        column.getType().name().toLowerCase(),
                        column.getTypeLength()
                );
                Element attrElement = tableElement.addElement("attr");
                table.addAttribute(tableAttribute);
                attrElement.addAttribute("name", column.getColumnName());
                attrElement.addAttribute("type", column.getType().name().toLowerCase());
                if (column.getTypeLength() != -1) {
                    attrElement.addAttribute("length", Integer.toString(column.getTypeLength()));
                }
            }
            database.addTable(table);
            DBManager dbManager = DBManager.getInstance();
            dbManager.write(database);
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
            DBManager dbManager = DBManager.getInstance();
            dbManager.deleteDatabase(stmt.getDBName());
            if (user.getCurrentDB() != null && user.getCurrentDB().getName().equals(stmt.getDBName())) {
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
                    user.getCurrentDB().removeTable(stmt.getTableName());
                    DBManager dbManager = DBManager.getInstance();
                    dbManager.write(user.getCurrentDB());
                    return String.format("Table %s deleted.", stmt.getTableName());
                }
            }
            throw new DBProcessException("The target table is not exist.");
        }
        throw new DBProcessException("Unknown error.");
    }
}
