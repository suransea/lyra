package com.sea.lyrad.db.table;

import java.util.ArrayList;
import java.util.List;

/**
 * 只含属性不含数据的数据库表对象
 */
public class Table {

    private String name;
    private List<TableAttribute> attributes;//表属性，即列(Column)

    public Table(String name) {
        this.name = name;
        attributes = new ArrayList<>();
    }

    /**
     * 获取全部属性
     */
    public List<TableAttribute> getAttributes() {
        return attributes;
    }

    public String getName() {
        return name;
    }

    /**
     * 获取指定属性名的属性对象
     */
    public TableAttribute getAttribute(String name) {
        for (TableAttribute attribute : attributes) {
            if (attribute.getName().equals(name)) {
                return attribute;
            }
        }
        return null;
    }

    /**
     * 添加表属性
     *
     * @param tableAttribute 要添加的属性
     * @return true (as specified by Collection.add)
     */
    public boolean addAttribute(TableAttribute tableAttribute) {
        return attributes.add(tableAttribute);
    }
}
