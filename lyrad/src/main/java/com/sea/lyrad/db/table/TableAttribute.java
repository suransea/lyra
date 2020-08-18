package com.sea.lyrad.db.table;

import com.sea.lyrad.exec.DBProcessException;

public class TableAttribute {
    private String name;
    private DataType type;
    private int length = -1;//类型长度，-1表示长度不限

    public TableAttribute(String name, String type, String length) {
        this.name = name;
        for (DataType dataType : DataType.values()) {
            if (dataType.name().toLowerCase().equals(type)) {
                this.type = dataType;
                break;
            }
        }
        if (length == null) return;
        this.length = Integer.parseInt(length);
    }

    public TableAttribute(String name, String type, int length) {
        this.name = name;
        for (DataType dataType : DataType.values()) {
            if (dataType.name().toLowerCase().equals(type)) {
                this.type = dataType;
                break;
            }
        }
        this.length = length;
    }

    public String getName() {
        return name;
    }

    public DataType getType() {
        return type;
    }

    public int getLength() {
        return length;
    }

    /**
     * 检测值是否符合该属性约束
     *
     * @param value 待检测的值
     * @throws DBProcessException 不符合约束时
     */
    public void check(String value) throws DBProcessException {
        if (type == DataType.VARCHAR) {
            if (length == -1) return;
            if (value.length() > length) {
                throw new DBProcessException(String.format("The length of '%s' is outsize.", value));
            }
        } else if (type == DataType.INT) {
            try {
                Integer.parseInt(value);
            } catch (NumberFormatException e) {
                throw new DBProcessException(String.format("The format of value '%s' is not right.", value));
            }
        }
    }
}
