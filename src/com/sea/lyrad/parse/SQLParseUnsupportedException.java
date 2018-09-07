package com.sea.lyrad.parse;

import com.sea.lyrad.lex.token.TokenType;

public class SQLParseUnsupportedException extends Exception {
    private static final String MESSAGE = "Not supported token '%s'.";

    public SQLParseUnsupportedException(TokenType tokenType) {
        super(String.format(MESSAGE, tokenType.toString()));
    }
}
