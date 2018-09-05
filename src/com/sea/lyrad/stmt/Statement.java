package com.sea.lyrad.stmt;

import com.sea.lyrad.DBProcessException;
import com.sea.lyrad.User;

public interface Statement {
    String execute(User user) throws DBProcessException;
}
