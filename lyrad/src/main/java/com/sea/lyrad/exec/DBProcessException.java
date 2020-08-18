package com.sea.lyrad.exec;

/**
 * 数据库执行过程中的异常
 */
public class DBProcessException extends Exception {

    public DBProcessException(String message) {
        super(message);
    }
}
