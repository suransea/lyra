package com.sea.pxxd.stmt;

import com.sea.pxxd.DBManager;
import com.sea.pxxd.DBProcessException;
import com.sea.pxxd.SQLParseException;
import com.sea.pxxd.User;
import com.sea.pxxd.db.Database;
import com.sea.pxxd.db.Table;
import org.dom4j.Document;
import org.dom4j.Element;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreateTable implements Statement {

    public Table getTable() {
        return table;
    }

    private String sql;
    private Table table;

    public CreateTable(Matcher matcher) throws SQLParseException {
        sql = matcher.group(0);
        table = new Table(matcher.group(1));
        String[] attrs = matcher.group(2).trim().split(",");
        Pattern pattern = Pattern.compile("(\\w+)\\s+(varchar|int)\\s*(?:\\(\\s*(\\d+)\\s*\\)\\s*)*");
        Matcher subMatcher;
        for (String attr : attrs) {
            subMatcher = pattern.matcher(attr.trim());
            if (!subMatcher.matches()) {
                throw new SQLParseException("The format of the subSQL is not right.");
            }
            String name = subMatcher.group(1);
            switch (subMatcher.group(2)) {
                case "varchar": {
                    int length = Integer.parseInt(subMatcher.group(3));
                    if (length <= 0) {
                        throw new SQLParseException("The length should max to zero.");
                    }
                    table.getAttributes().add(
                            new Table.Attribute(
                                    name,
                                    Table.Attribute.Type.VARCHAR,
                                    length
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
    }

    @Override
    public String execute(User user) throws DBProcessException {
        Database database = user.getCurrentDB();
        if (database == null) {
            throw new DBProcessException("Please use a database firstly.");
        }
        if (database.getTable(table.getName()) != null) {
            throw new DBProcessException("The table name is already exist.");
        }
        Document document = database.getDocument();
        Element root = document.getRootElement();
        Element tableElement = root.addElement("table");
        tableElement.addAttribute("name", table.getName());
        for (Table.Attribute it : table.getAttributes()) {
            Element attrElement = tableElement.addElement("attr");
            attrElement.addAttribute("name", it.getName());
            attrElement.addAttribute("type", it.getType().name().toLowerCase());
            if (it.getLength() != -1) {
                attrElement.addAttribute("length", Integer.toString(it.getLength()));
            }
        }
        DBManager dbManager = new DBManager();
        dbManager.write(database);
        return "Table created.";
    }

    @Override
    public String toString() {
        return sql;
    }
}
