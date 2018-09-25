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
    public List<Attribute> getAttributes() {
        return attributes;
    }

    private List<Attribute> attributes;

    public String getName() {
        return name;
    }

    /**
     * 获取指定属性名的属性对象
     */
    public Attribute getAttribute(String name) {
        for (Attribute attribute : attributes) {
            if (attribute.getName().equals(name)) {
                return attribute;
            }
        }
        return null;
    }

    public boolean addAttribute(Attribute attribute) {
        return attributes.add(attribute);
    }

    public Table(String name) {
        this.name = name;
        attributes = new ArrayList<>();
    }
}
