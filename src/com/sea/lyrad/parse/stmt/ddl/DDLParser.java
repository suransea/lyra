package com.sea.lyrad.parse.stmt.ddl;

import com.sea.lyrad.lex.Lexer;
import com.sea.lyrad.lex.analyze.UnterminatedCharException;
import com.sea.lyrad.lex.token.*;
import com.sea.lyrad.parse.SQLParseException;
import com.sea.lyrad.parse.SQLParseUnsupportedException;
import com.sea.lyrad.parse.SQLParser;
import com.sea.lyrad.parse.stmt.SQLStatement;
import com.sea.lyrad.parse.stmt.context.Column;

public class DDLParser extends SQLParser {
    public DDLParser(Lexer lexer) {
        super(lexer);
    }

    @Override
    public SQLStatement parse() throws SQLParseException, UnterminatedCharException, SQLParseUnsupportedException {
        Token token = lexer.getToken();
        if (token.getType().equals(Keyword.CREATE)) {
            return parseCreate();
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
            Lexer columnLexer = new Lexer(skipParentheses());
            SQLParser columnParser = new SQLParser(columnLexer);
            columnLexer.nextToken();
            columnParser.accept(Symbol.LEFT_PAREN);
            while (true) {
                Column column = new Column();
                column.setColumnName(columnLexer.getToken().getLiterals());
                columnParser.accept(Literals.IDENTIFIER);
                if (columnParser.equalAny(Keyword.INT)) {
                    column.setType(Keyword.INT);
                    columnParser.accept(Keyword.INT);
                } else if (columnParser.equalAny(Keyword.VARCHAR)) {
                    column.setType(Keyword.VARCHAR);
                    columnParser.accept(Keyword.VARCHAR);
                    columnParser.accept(Symbol.LEFT_PAREN);
                    token = columnLexer.getToken();
                    columnParser.accept(Literals.INT);
                    column.setTypeLength(Integer.parseInt(token.getLiterals()));
                    columnParser.accept(Symbol.RIGHT_PAREN);
                } else {
                    throw new SQLParseUnsupportedException(columnLexer.getToken().getType());
                }
                statement.getColumns().add(column);
                if (columnLexer.getToken().getType() == Symbol.RIGHT_PAREN) {
                    break;
                }
                columnParser.accept(Symbol.COMMA);
            }
            accept(Symbol.SEMI);
            accept(Assist.END);
            return statement;
        }
        throw new SQLParseUnsupportedException(token.getType());
    }
}
