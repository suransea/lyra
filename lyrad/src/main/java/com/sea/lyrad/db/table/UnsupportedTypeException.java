package com.sea.lyrad.db.table;

/**
 * 不支持的数据类型
 */
public class UnsupportedTypeException extends Exception {
    public UnsupportedTypeException(String message) {
        super(message);
    }
}
