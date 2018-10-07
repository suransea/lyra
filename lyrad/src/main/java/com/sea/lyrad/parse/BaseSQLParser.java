package com.sea.lyrad.parse;

import com.sea.lyrad.lex.Lexer;
import com.sea.lyrad.lex.analyze.UnterminatedCharException;
import com.sea.lyrad.lex.token.Assist;
import com.sea.lyrad.lex.token.TokenType;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public abstract class BaseSQLParser {
    protected Lexer lexer;

    public BaseSQLParser(String sql) {
        lexer = new Lexer(sql);
    }

    public BaseSQLParser(Lexer lexer) {
        this.lexer = lexer;
    }

    public void accept(TokenType tokenType) throws SQLParseException, UnterminatedCharException {
        if (lexer.getToken().getType() != tokenType) {
            throw new SQLParseException(lexer, tokenType);
        }
        lexer.nextToken();
    }

    public boolean equalAny(TokenType... tokenTypes) {
        for (TokenType each : tokenTypes) {
            if (each == lexer.getToken().getType()) {
                return true;
            }
        }
        return false;
    }

    public boolean skipIfEqual(TokenType... tokenTypes) throws SQLParseException, UnterminatedCharException {
        if (equalAny(tokenTypes)) {
            lexer.nextToken();
            return true;
        }
        return false;
    }

    public void skipAll(TokenType... tokenTypes) throws SQLParseException, UnterminatedCharException {
        Set<TokenType> tokenTypeSet = new HashSet<>(Arrays.asList(tokenTypes));
        while (tokenTypeSet.contains(lexer.getToken().getType())) {
            lexer.nextToken();
        }
    }

    public void skipUntil(TokenType... tokenTypes) throws SQLParseException, UnterminatedCharException {
        Set<TokenType> tokenTypeSet = new HashSet<>(Arrays.asList(tokenTypes));
        tokenTypeSet.add(Assist.END);
        while (!tokenTypeSet.contains(lexer.getToken().getType())) {
            lexer.nextToken();
        }
    }
}
