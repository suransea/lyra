package com.sea.pxxd.stmt;

import com.sea.pxxd.DBManager;
import com.sea.pxxd.DBProcessException;

public interface Statement {
    String execute(DBManager dbManager) throws DBProcessException;
}
