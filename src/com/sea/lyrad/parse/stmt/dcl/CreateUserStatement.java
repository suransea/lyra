package com.sea.lyrad.parse.stmt.dcl;

public class CreateUserStatement extends DCLStatement {
    public CreateUserStatement(String sql) {
        super(sql);
    }

    private String username;
    private String password;

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
