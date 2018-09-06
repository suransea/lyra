package com.sea.lyrad.lex.token;


/**
 * Token
 */
public class Token {

    private TokenType type;
    private String literals;

    public TokenType getType() {
        return type;
    }

    public String getLiterals() {
        return literals;
    }

    public int getEndPosition() {
        return endPosition;
    }

    private int endPosition;

    public Token(TokenType type, String literals, int endPosition) {
        this.type = type;
        this.literals = literals;
        this.endPosition = endPosition;
    }
}
