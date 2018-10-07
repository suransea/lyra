package com.sea.lyrad.db;

import com.sea.lyrad.db.table.Table;
import com.sea.lyrad.exec.DBProcessException;
import com.sea.lyrad.parse.SQLParseException;
import com.sea.lyrad.stmt.common.WhereExpression;
import com.sea.lyrad.util.XMLUtil;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;

import java.util.*;

public class Database {
    private String name;
    private List<Table> tables;
    private Document document;

    public Database(String name, Document document) {
        this.name = name;
        this.document = document;
        tables = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<Table> getTables() {
        return tables;
    }

    /**
     * @return 数据库对应的xml文档对象
     */
    public Document getDocument() {
        return document;
    }

    /**
     * 获取指定表名的Table对象
     *
     * @param name 表名
     * @return table or null
     */
    public Table getTable(String name) {
        for (Table table : tables) {
            if (table.getName().equals(name)) {
                return table;
            }
        }
        return null;
    }

    public void removeTable(String name) {
        tables.removeIf(x -> x.getName().equals(name));
    }

    public boolean addTable(Table table) {
        return tables.add(table);
    }

    /**
     * 获取全部行的数据
     *
     * @param tableName 表名
     * @return 数据
     * @throws DBProcessException 表名不存在时
     */
    public List<Map<String, String>> getRows(String tableName) throws DBProcessException {
        List<Map<String, String>> result = new ArrayList<>();
        Element rootElement = document.getRootElement();
        Element tableElement = XMLUtil.getTableElement(rootElement, tableName);
        if (tableElement == null) {
            throw new DBProcessException("The target table is not exist.");
        }
        for (Iterator<Element> it = tableElement.elementIterator("data"); it.hasNext(); ) {
            Map<String, String> values = new HashMap<>();
            for (Iterator<Attribute> attrIt = it.next().attributeIterator(); attrIt.hasNext(); ) {
                Attribute attribute = attrIt.next();
                values.put(attribute.getName(), attribute.getValue());
            }
            result.add(values);
        }
        return result;
    }

    public List<Map<String, String>> getRows(String tableName, WhereExpression expression) throws SQLParseException, DBProcessException {
        List<Map<String, String>> result = new ArrayList<>();
        Element rootElement = document.getRootElement();
        Element tableElement = XMLUtil.getTableElement(rootElement, tableName);
        if (tableElement == null) {
            throw new DBProcessException("The target table is not exist.");
        }
        for (Iterator<Element> it = tableElement.elementIterator("data"); it.hasNext(); ) {
            Map<String, String> values = new HashMap<>();
            for (Iterator<Attribute> attrIt = it.next().attributeIterator(); attrIt.hasNext(); ) {
                Attribute attribute = attrIt.next();
                values.put(attribute.getName(), attribute.getValue());
            }
            if (expression.isMatched(values)) result.add(values);
        }
        return result;
    }
}
