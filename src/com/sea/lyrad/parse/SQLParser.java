package com.sea.lyrad.parse;

import com.sea.lyrad.lex.Lexer;
import com.sea.lyrad.lex.analyze.UnterminatedCharException;
import com.sea.lyrad.lex.token.*;
import com.sea.lyrad.parse.stmt.SQLStatement;
import com.sea.lyrad.parse.stmt.dal.DALParser;
import com.sea.lyrad.parse.stmt.ddl.DDLParser;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class SQLParser {

    protected Lexer lexer;

    public SQLParser(Lexer lexer) {
        this.lexer = lexer;
    }

    public SQLStatement parse() throws SQLParseException, UnterminatedCharException, SQLParseUnsupportedException {
        Token token = lexer.nextToken();
        if (equalAny(Keyword.USE, Keyword.SHOW, Keyword.DESCRIBE)) {
            return new DALParser(lexer).parse();
        } else if (equalAny(Keyword.CREATE, Keyword.ALTER, Keyword.DROP, Keyword.TRUNCATE)) {
            return new DDLParser(lexer).parse();
        } else {
            throw new SQLParseUnsupportedException(token.getType());
        }
    }

    /**
     * skip all tokens that inside parentheses.
     *
     * @return skipped string
     */
    public String skipParentheses() throws SQLParseException, UnterminatedCharException {
        StringBuilder result = new StringBuilder();
        int count = 0;
        if (Symbol.LEFT_PAREN == lexer.getToken().getType()) {
            int beginPosition = lexer.getToken().getEndPosition();
            result.append(Symbol.LEFT_PAREN.getLiterals());
            lexer.nextToken();
            while (true) {
                if (Assist.END == lexer.getToken().getType() || (Symbol.RIGHT_PAREN == lexer.getToken().getType() && 0 == count)) {
                    if (lexer.getToken().getType().equals(Assist.END)) {
                        throw new UnterminatedCharException('(');
                    }
                    break;
                }
                if (Symbol.LEFT_PAREN == lexer.getToken().getType()) {
                    count++;
                } else if (Symbol.RIGHT_PAREN == lexer.getToken().getType()) {
                    count--;
                }
                lexer.nextToken();
            }
            result.append(lexer.getContent(), beginPosition, lexer.getToken().getEndPosition());
            lexer.nextToken();
        }
        return result.toString();
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

    public void unsupportedIfEqual(TokenType... tokenTypes) throws SQLParseUnsupportedException {
        if (equalAny(tokenTypes)) {
            throw new SQLParseUnsupportedException(lexer.getToken().getType());
        }
    }

    /**
     * Throw unsupported exception if current token not equals one of input tokens.
     *
     * @param tokenTypes to be adjusted token types
     */
    public void unsupportedIfNotSkip(TokenType... tokenTypes) throws SQLParseException, UnterminatedCharException, SQLParseUnsupportedException {
        if (!skipIfEqual(tokenTypes)) {
            throw new SQLParseUnsupportedException(lexer.getToken().getType());
        }
    }
}

