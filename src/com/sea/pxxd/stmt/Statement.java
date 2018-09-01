package com.sea.pxxd.stmt;

import com.sea.pxxd.DBProcessException;

public interface Statement {
    String execute() throws DBProcessException;
}
