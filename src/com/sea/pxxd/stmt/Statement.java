package com.sea.pxxd.stmt;

import com.sea.pxxd.DBProcessException;
import com.sea.pxxd.User;

public interface Statement {
    String execute(User user) throws DBProcessException;
}
