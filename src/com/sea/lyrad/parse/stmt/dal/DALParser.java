package com.sea.lyrad.parse.stmt.dal;

import com.sea.lyrad.lex.Lexer;
import com.sea.lyrad.lex.analyze.UnterminatedCharException;
import com.sea.lyrad.lex.token.*;
import com.sea.lyrad.parse.SQLParseException;
import com.sea.lyrad.parse.SQLParseUnsupportedException;
import com.sea.lyrad.parse.SQLParser;
import com.sea.lyrad.parse.stmt.SQLStatement;

public class DALParser extends SQLParser {

    public DALParser(Lexer lexer) {
        super(lexer);
    }

    @Override
    public SQLStatement parse() throws SQLParseException, UnterminatedCharException, SQLParseUnsupportedException {
        Token token = lexer.getToken();
        if (token.getType() == Keyword.USE) {
            return parseUse();
        } else if (token.getType() == Keyword.SHOW) {
            return parseShow();
        }
        throw new SQLParseUnsupportedException(lexer.getToken().getType());
    }

    private SQLStatement parseUse() throws SQLParseException, UnterminatedCharException {
        accept(Keyword.USE);
        Token token = lexer.getToken();
        if (token.getType() == Literals.IDENTIFIER) {
            String dbName = token.getLiterals();
            accept(Literals.IDENTIFIER);
            accept(Symbol.SEMI);
            accept(Assist.END);
            return new UseStatement(lexer.getContent(), dbName);
        }
        throw new SQLParseException(lexer);
    }

    private SQLStatement parseShow() throws SQLParseException, UnterminatedCharException {
        accept(Keyword.SHOW);
        Token token = lexer.getToken();
        ShowStatement showStatement = new ShowStatement();
        if (token.getType() == Keyword.DATABASES) {
            showStatement.setItem(Keyword.DATABASES);
            return showStatement;
        } else if (token.getType() == Keyword.TABLES) {
            showStatement.setItem(Keyword.TABLES);
            return showStatement;
        }
        throw new SQLParseException(lexer, Keyword.SHOW);
    }
}
