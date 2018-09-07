package com.sea.lyrad.exec;

import com.sea.lyrad.db.Database;
import com.sea.lyrad.db.Table;
import com.sea.lyrad.parse.stmt.context.Column;
import com.sea.lyrad.parse.stmt.dml.DMLStatement;
import com.sea.lyrad.parse.stmt.dml.InsertStatement;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.tree.DefaultElement;

import java.util.*;

public class DMLExecutor extends SQLExecutor {
    private User user;
    private DMLStatement statement;

    public String execute(User user, DMLStatement statement) throws DBProcessException {
        this.user = user;
        this.statement = statement;
        if (statement instanceof InsertStatement) {
            return executeInsert();
        }
        throw new DBProcessException("Unsupported DML statement.");
    }

    private String executeInsert() throws DBProcessException {
        InsertStatement stmt = (InsertStatement) statement;
        Database database = user.getCurrentDB();
        if (database == null) {
            throw new DBProcessException("Please select a database firstly.");
        }
        Document document = database.getDocument();
        Element tableElement = null;
        for (Iterator<Element> it = document.getRootElement().elementIterator("table"); it.hasNext(); ) {
            Element element = it.next();
            if (element.attributeValue("name").equals(stmt.getTableName())) {
                tableElement = element;
                break;
            }
        }
        if (tableElement == null) {
            throw new DBProcessException("The target table is not exist.");
        }
        Table table = database.getTable(stmt.getTableName());
        List<Column> columns = stmt.getColumns();
        if (columns.size() != 0) {
            for (Column column : columns) {
                if (table.getAttribute(column.getColumnName()) == null) {
                    throw new DBProcessException(String.format("The column %s is not exist.", column.getColumnName()));
                }
            }
        } else {
            for (Table.Attribute attr : table.getAttributes()) {
                Column column = new Column();
                column.setColumnName(attr.getName());
                columns.add(column);
            }
        }
        List<Element> elements = new ArrayList<>();
        for (List<String> value : stmt.getValues()) {
            if (columns.size() != value.size()) {
                throw new DBProcessException("The values count is not matching.");
            }
            Map<String, String> data = new HashMap<>();
            for (int i = 0; i < value.size(); i++) {
                data.put(columns.get(i).getColumnName(), value.get(i));
            }
            Element element = new DefaultElement("data");
            for (int i = 0; i < table.getAttributes().size(); i++) {
                Table.Attribute attribute = table.getAttributes().get(i);
                String subValue = data.get(attribute.getName());
                if (subValue == null) {
                    element.addAttribute(attribute.getName(), "(none)");
                    continue;
                }
                if (attribute.getType() == Table.Attribute.Type.INT) {
                    try {
                        int number = Integer.parseInt(subValue);
                        element.addAttribute(attribute.getName(), Integer.toString(number));
                    } catch (NumberFormatException e) {
                        throw new DBProcessException("The format of value is not right.");
                    }
                } else if (attribute.getType() == Table.Attribute.Type.VARCHAR) {
                    if (subValue.length() > attribute.getLength()) {
                        throw new DBProcessException("The length is outsize.");
                    }
                    element.addAttribute(attribute.getName(), subValue);
                }
            }
            elements.add(element);
        }
        elements.forEach(tableElement::add);
        int count = elements.size();
        DBManager dbManager = new DBManager();
        dbManager.write(database);
        return String.format("%d item(s) inserted.", count);
    }
}
