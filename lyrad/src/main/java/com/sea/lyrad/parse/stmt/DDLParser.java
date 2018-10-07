package com.sea.lyrad.parse.stmt;

import com.sea.lyrad.lex.Lexer;
import com.sea.lyrad.lex.analyze.UnterminatedCharException;
import com.sea.lyrad.lex.token.*;
import com.sea.lyrad.parse.BaseSQLParser;
import com.sea.lyrad.parse.SQLParseException;
import com.sea.lyrad.parse.SQLParseUnsupportedException;
import com.sea.lyrad.parse.SQLParser;
import com.sea.lyrad.stmt.SQLStatement;
import com.sea.lyrad.stmt.common.Column;
import com.sea.lyrad.stmt.ddl.CreateStatement;
import com.sea.lyrad.stmt.ddl.DropStatement;

public class DDLParser extends BaseSQLParser implements SQLParser {
    public DDLParser(Lexer lexer) {
        super(lexer);
    }

    @Override
    public SQLStatement parse() throws SQLParseException, UnterminatedCharException, SQLParseUnsupportedException {
        Token token = lexer.getToken();
        if (token.getType().equals(Keyword.CREATE)) {
            return parseCreate();
        } else if (token.getType() == Keyword.DROP) {
            return parseDrop();
        }
        throw new SQLParseUnsupportedException(lexer.getToken().getType());
    }

    private SQLStatement parseCreate() throws SQLParseException, UnterminatedCharException, SQLParseUnsupportedException {
        CreateStatement statement = new CreateStatement(lexer.getContent());
        accept(Keyword.CREATE);
        Token token = lexer.getToken();
        if (token.getType().equals(Keyword.DATABASE)) {
            accept(Keyword.DATABASE);
            statement.setItem(Keyword.DATABASE);
            token = lexer.getToken();
            statement.setDBName(token.getLiterals());
            accept(Literals.IDENTIFIER);
            accept(Symbol.SEMI);
            accept(Assist.END);
            return statement;
        }
        if (token.getType().equals(Keyword.TABLE)) {
            accept(Keyword.TABLE);
            statement.setItem(Keyword.TABLE);
            statement.setTableName(lexer.getToken().getLiterals());
            accept(Literals.IDENTIFIER);

            accept(Symbol.LEFT_PAREN);
            while (true) {
                Column column = new Column();
                column.setColumnName(lexer.getToken().getLiterals());
                accept(Literals.IDENTIFIER);
                if (equalAny(Keyword.INT)) {
                    column.setType(Keyword.INT);
                    accept(Keyword.INT);
                } else if (equalAny(Keyword.VARCHAR)) {
                    column.setType(Keyword.VARCHAR);
                    accept(Keyword.VARCHAR);
                    accept(Symbol.LEFT_PAREN);
                    token = lexer.getToken();
                    accept(Literals.INT);
                    column.setTypeLength(Integer.parseInt(token.getLiterals()));
                    accept(Symbol.RIGHT_PAREN);
                } else {
                    throw new SQLParseUnsupportedException(lexer.getToken().getType());
                }
                statement.getColumns().add(column);
                if (lexer.getToken().getType() == Symbol.RIGHT_PAREN) {
                    accept(Symbol.RIGHT_PAREN);
                    break;
                }
                accept(Symbol.COMMA);
            }

            accept(Symbol.SEMI);
            accept(Assist.END);
            return statement;
        }
        throw new SQLParseUnsupportedException(token.getType());
    }

    private SQLStatement parseDrop() throws SQLParseException, UnterminatedCharException {
        DropStatement statement = new DropStatement(lexer.getContent());
        accept(Keyword.DROP);
        Token token = lexer.getToken();
        if (token.getType() == Keyword.DATABASE) {
            statement.setItem(Keyword.DATABASE);
            accept(Keyword.DATABASE);
            statement.setDBName(lexer.getToken().getLiterals());
            accept(Literals.IDENTIFIER);
            accept(Symbol.SEMI);
            accept(Assist.END);
            return statement;
        } else if (token.getType() == Keyword.TABLE) {
            statement.setItem(Keyword.TABLE);
            accept(Keyword.TABLE);
            statement.setTableName(lexer.getToken().getLiterals());
            accept(Literals.IDENTIFIER);
            accept(Symbol.SEMI);
            accept(Assist.END);
            return statement;
        }
        throw new SQLParseException(lexer);
    }
}
