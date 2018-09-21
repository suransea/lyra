package com.sea.lyrad.db;

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
        for (Attribute attr : attributes) {
            if (attr.getName().equals(name)) {
                return attr;
            }
        }
        return null;
    }

    public Table(String name) {
        this.name = name;
        attributes = new ArrayList<>();
    }

    public static class Attribute {

        public enum Type {
            INT,
            VARCHAR
        }

        private String name;
        private Type type;

        public String getName() {
            return name;
        }

        public Type getType() {
            return type;
        }

        public int getLength() {
            return length;
        }

        private int length;

        public Attribute(String name, Type type, int length) {
            this.name = name;
            this.type = type;
            this.length = length;
        }

        public Attribute(String name, Type type) {
            this.name = name;
            this.type = type;
            this.length = -1;
        }
    }
}
