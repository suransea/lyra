package com.sea.lyrad.db.table;

import java.util.ArrayList;
import java.util.List;

/**
 * 只含属性不含数据的数据库表对象
 */
public class Table {

    private String name;

    /**
     * 获取全部属性
     */
    public List<TableAttribute> getAttributes() {
        return attributes;
    }

    private List<TableAttribute> attributes;

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

    public boolean addAttribute(TableAttribute tableAttribute) {
        return attributes.add(tableAttribute);
    }

    public Table(String name) {
        this.name = name;
        attributes = new ArrayList<>();
    }
}
