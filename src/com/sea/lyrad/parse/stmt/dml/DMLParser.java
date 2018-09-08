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
        } else if (token.getType() == Keyword.UPDATE) {
            return parseUpdate();
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
                if (equalAny(Literals.INT, Literals.STRING)) {
                    lexer.nextToken();
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
        parseWhere(statement);
        accept(Symbol.SEMI);
        accept(Assist.END);
        return statement;
    }

    private SQLStatement parseUpdate() throws SQLParseException, UnterminatedCharException, SQLParseUnsupportedException {
        UpdateStatement statement = new UpdateStatement(lexer.getContent());
        accept(Keyword.UPDATE);
        statement.setTableName(lexer.getToken().getLiterals());
        accept(Literals.IDENTIFIER);
        accept(Keyword.SET);
        while (true) {
            Column column = new Column();
            column.setColumnName(lexer.getToken().getLiterals());
            accept(Literals.IDENTIFIER);
            accept(Symbol.EQ);
            if (equalAny(Literals.INT, Literals.STRING)) {
                column.setValue(lexer.getToken().getLiterals());
                lexer.nextToken();
            }
            statement.getColumns().add(column);
            if (lexer.getToken().getType() != Symbol.COMMA) {
                break;
            }
            accept(Symbol.COMMA);
        }
        if (lexer.getToken().getType() == Symbol.SEMI) {
            accept(Symbol.SEMI);
            accept(Assist.END);
            return statement;
        }
        accept(Keyword.WHERE);
        parseWhere(statement);
        accept(Symbol.SEMI);
        accept(Assist.END);
        return statement;
    }

    private void parseWhere(DMLStatement statement) throws SQLParseException, UnterminatedCharException, SQLParseUnsupportedException {
        List<Condition> conditions = statement.getConditions();
        List<Keyword> connectors = statement.getConnectors();
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
            conditions.add(condition);
            if (lexer.getToken().getType() == Symbol.SEMI) {
                break;
            }
            if (equalAny(Keyword.AND, Keyword.OR)) {
                connectors.add((Keyword) lexer.getToken().getType());
                lexer.nextToken();
            }
        }
    }
}
