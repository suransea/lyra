package com.sea.lyrad.parse;

import com.sea.lyrad.lex.Lexer;
import com.sea.lyrad.lex.token.TokenType;

public class SQLParseException extends Exception {
    private static final String UNMATCH_MESSAGE = "SQL syntax error, expected token is '%s', actual token is '%s', literals is '%s'.";

    private static final String TOKEN_ERROR_MESSAGE = "SQL syntax error, token is '%s', literals is '%s'.";

    public SQLParseException(String message, Object... args) {
        super(String.format(message, args));
    }

    public SQLParseException(Lexer lexer, TokenType expectedTokenType) {
        super(String.format(UNMATCH_MESSAGE, expectedTokenType, lexer.getToken().getType(), lexer.getToken().getLiterals()));
    }

    public SQLParseException(Lexer lexer) {
        super(String.format(TOKEN_ERROR_MESSAGE, lexer.getToken().getType(), lexer.getToken().getLiterals()));
    }
}
