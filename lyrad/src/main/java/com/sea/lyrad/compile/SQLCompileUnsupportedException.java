package com.sea.lyrad.compile;

import com.sea.lyrad.lex.token.TokenType;

public class SQLCompileUnsupportedException extends Exception {
    private static final String MESSAGE = "Not supported token '%s' to compile. ";

    public SQLCompileUnsupportedException(TokenType tokenType) {
        super(String.format(MESSAGE, tokenType.toString()));
    }
}
