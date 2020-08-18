package com.sea.lyrad.stmt.dcl;

import com.sea.lyrad.stmt.BaseSQLStatement;
import com.sea.lyrad.stmt.SQLStatement;

public class DCLStatement extends BaseSQLStatement implements SQLStatement {
    private String username;
    private String password;

    public DCLStatement(String sql) {
        super(sql);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
