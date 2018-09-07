package com.sea.lyrad.parse.stmt.dcl;

public class AlterUserStatement extends DCLStatement {
    public AlterUserStatement(String sql) {
        super(sql);
    }

    private String username;

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

    private String password;
}
