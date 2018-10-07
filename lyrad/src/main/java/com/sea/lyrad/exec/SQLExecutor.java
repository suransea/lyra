package com.sea.lyrad.exec;

import com.sea.lyrad.parse.SQLParseException;
import com.sea.lyrad.stmt.SQLStatement;

public interface SQLExecutor {
    /**
     * 以用户user执行语句statement并返回执行结果
     */
    String execute(User user, SQLStatement statement) throws DBProcessException, SQLParseException;
}
