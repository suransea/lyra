package com.sea.pxxd.db;

import java.util.ArrayList;
import java.util.List;

public class Table {

    private String name;

    public List<Attribute> getAttributes() {
        return attributes;
    }

    private List<Attribute> attributes;

    public String getName() {
        return name;
    }

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
