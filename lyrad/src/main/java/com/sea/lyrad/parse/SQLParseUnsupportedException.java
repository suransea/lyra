package com.sea.lyrad.parse;

import com.sea.lyrad.lex.token.TokenType;

/**
 * 不支持的词素类型
 */
public class SQLParseUnsupportedException extends Exception {
    private static final String MESSAGE = "Not supported token '%s'. ";

    public SQLParseUnsupportedException(TokenType tokenType) {
        super(String.format(MESSAGE, tokenType.toString()));
    }
}
