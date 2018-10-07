package com.sea.lyrad.parse;

import com.sea.lyrad.lex.Lexer;
import com.sea.lyrad.lex.analyze.UnterminatedCharException;
import com.sea.lyrad.lex.token.Keyword;
import com.sea.lyrad.lex.token.Token;
import com.sea.lyrad.lex.token.TokenType;
import com.sea.lyrad.parse.stmt.*;

public class SQLParserFactory {
    private Lexer lexer;

    public SQLParserFactory() {
    }

    public SQLParser createInstance(String sql) throws SQLParseException, UnterminatedCharException, SQLParseUnsupportedException {
        lexer = new Lexer(sql);
        Token token = lexer.nextToken();
        if (equalAny(Keyword.USE, Keyword.SHOW, Keyword.DESCRIBE)) {
            return new DALParser(lexer);
        } else if (equalAny(Keyword.TRUNCATE)) {
            return new DDLParser(lexer);
        } else if (equalAny(Keyword.CREATE, Keyword.ALTER, Keyword.DROP)) {
            lexer.nextToken();
            Lexer newLexer = new Lexer(lexer.getContent());
            newLexer.nextToken();
            if (equalAny(Keyword.USER)) {
                return new DCLParser(newLexer);
            } else {
                return new DDLParser(newLexer);
            }
        } else if (equalAny(Keyword.DENY, Keyword.GRANT)) {
            return new DCLParser(lexer);
        } else if (equalAny(Keyword.INSERT, Keyword.DELETE, Keyword.UPDATE)) {
            return new DMLParser(lexer);
        } else if (equalAny(Keyword.SELECT)) {
            return new DQLParser(lexer);
        } else {
            throw new SQLParseUnsupportedException(token.getType());
        }
    }

    private boolean equalAny(TokenType... tokenTypes) {
        for (TokenType each : tokenTypes) {
            if (each == lexer.getToken().getType()) {
                return true;
            }
        }
        return false;
    }
}
