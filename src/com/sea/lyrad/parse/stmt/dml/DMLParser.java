package com.sea.lyrad.parse.stmt.dml;

import com.sea.lyrad.lex.Lexer;
import com.sea.lyrad.lex.analyze.UnterminatedCharException;
import com.sea.lyrad.lex.token.*;
import com.sea.lyrad.parse.SQLParseException;
import com.sea.lyrad.parse.SQLParseUnsupportedException;
import com.sea.lyrad.parse.SQLParser;
import com.sea.lyrad.parse.stmt.SQLStatement;
import com.sea.lyrad.parse.stmt.context.Column;
import com.sea.lyrad.parse.stmt.context.Condition;

import java.util.ArrayList;
import java.util.List;

public class DMLParser extends SQLParser {
    public DMLParser(Lexer lexer) {
        super(lexer);
    }

    public SQLStatement parse() throws SQLParseUnsupportedException, SQLParseException, UnterminatedCharException {
        Token token = lexer.getToken();
        if (token.getType() == Keyword.INSERT) {
            return parseInsert();
        } else if (token.getType() == Keyword.DELETE) {
            return parseDelete();
        }
        throw new SQLParseUnsupportedException(token.getType());
    }

    private SQLStatement parseInsert() throws SQLParseException, UnterminatedCharException {
        InsertStatement statement = new InsertStatement(lexer.getContent());
        accept(Keyword.INSERT);
        accept(Keyword.INTO);
        statement.setTableName(lexer.getToken().getLiterals());
        accept(Literals.IDENTIFIER);
        if (lexer.getToken().getType() == Symbol.LEFT_PAREN) {
            accept(Symbol.LEFT_PAREN);
        }
        while (lexer.getToken().getType() == Literals.IDENTIFIER) {
            Column column = new Column();
            column.setColumnName(lexer.getToken().getLiterals());
            statement.getColumns().add(column);
            accept(Literals.IDENTIFIER);
            if (lexer.getToken().getType() == Symbol.RIGHT_PAREN) {
                accept(Symbol.RIGHT_PAREN);
                break;
            }
            accept(Symbol.COMMA);
        }
        accept(Keyword.VALUES);
        while (lexer.getToken().getType() == Symbol.LEFT_PAREN) {
            accept(Symbol.LEFT_PAREN);
            List<String> value = new ArrayList<>();
            while (true) {
                value.add(lexer.getToken().getLiterals());
                if (lexer.getToken().getType() == Literals.INT) {
                    accept(Literals.INT);
                } else {
                    accept(Literals.STRING);
                }
                if (lexer.getToken().getType() == Symbol.RIGHT_PAREN) {
                    accept(Symbol.RIGHT_PAREN);
                    break;
                }
                accept(Symbol.COMMA);
            }
            statement.getValues().add(value);
            if (lexer.getToken().getType() != Symbol.SEMI) {
                accept(Symbol.COMMA);
            }
        }
        accept(Symbol.SEMI);
        accept(Assist.END);
        return statement;
    }

    private SQLStatement parseDelete() throws SQLParseException, UnterminatedCharException, SQLParseUnsupportedException {
        DeleteStatement statement = new DeleteStatement(lexer.getContent());
        accept(Keyword.DELETE);
        accept(Keyword.FROM);
        statement.setTableName(lexer.getToken().getLiterals());
        accept(Literals.IDENTIFIER);
        if (lexer.getToken().getType() == Symbol.SEMI) {
            accept(Symbol.SEMI);
            accept(Assist.END);
            return statement;
        }
        accept(Keyword.WHERE);
        while (true) {
            Condition condition = new Condition();
            Column column = new Column();
            column.setColumnName(lexer.getToken().getLiterals());
            condition.setColumn(column);
            accept(Literals.IDENTIFIER);
            if (lexer.getToken().getType() instanceof Symbol) {
                condition.setOperator((Symbol) lexer.getToken().getType());
                lexer.nextToken();
            } else {
                throw new SQLParseUnsupportedException(lexer.getToken().getType());
            }
            if (equalAny(Literals.STRING, Literals.INT)) {
                condition.setValue(lexer.getToken().getLiterals());
                lexer.nextToken();
            } else {
                throw new SQLParseUnsupportedException(lexer.getToken().getType());
            }
            statement.getConditions().add(condition);
            if (lexer.getToken().getType() == Symbol.SEMI) {
                break;
            }
            if (equalAny(Keyword.AND, Keyword.OR)) {
                statement.getConnectors().add((Keyword) lexer.getToken().getType());
                lexer.nextToken();
            }
        }
        accept(Symbol.SEMI);
        accept(Assist.END);
        return statement;
    }
}
