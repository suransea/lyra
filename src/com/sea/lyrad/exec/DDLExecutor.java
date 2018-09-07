package com.sea.lyrad.exec;

import com.sea.lyrad.db.Database;
import com.sea.lyrad.lex.token.Keyword;
import com.sea.lyrad.parse.stmt.context.Column;
import com.sea.lyrad.parse.stmt.ddl.CreateStatement;
import com.sea.lyrad.parse.stmt.ddl.DDLStatement;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.util.List;

public class DDLExecutor extends SQLExecutor {
    private User user;
    private DDLStatement statement;

    public String execute(User user, DDLStatement statement) throws DBProcessException {
        this.user = user;
        this.statement = statement;
        if (statement instanceof CreateStatement) {
            return executeCreate();
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
            return String.format("Table %s created.", stmt.getTableName());
        }
        throw new DBProcessException("Unknown error.");
    }
}
