package com.sea.lyrad.parse.stmt;

import com.sea.lyrad.lex.Lexer;
import com.sea.lyrad.lex.analyze.UnterminatedCharException;
import com.sea.lyrad.lex.token.*;
import com.sea.lyrad.parse.BaseSQLParser;
import com.sea.lyrad.parse.SQLParseException;
import com.sea.lyrad.parse.SQLParseUnsupportedException;
import com.sea.lyrad.parse.SQLParser;
import com.sea.lyrad.parse.express.WhereExpressionParser;
import com.sea.lyrad.stmt.SQLStatement;
import com.sea.lyrad.stmt.common.Column;
import com.sea.lyrad.stmt.dml.DeleteStatement;
import com.sea.lyrad.stmt.dml.InsertStatement;
import com.sea.lyrad.stmt.dml.UpdateStatement;

import java.util.ArrayList;
import java.util.List;

public class DMLParser extends BaseSQLParser implements SQLParser {
    private WhereExpressionParser whereExpressionParser;

    public DMLParser(Lexer lexer) {
        super(lexer);
        whereExpressionParser = new WhereExpressionParser(lexer);
    }

    public SQLStatement parse() throws SQLParseUnsupportedException, SQLParseException, UnterminatedCharException {
        Token token = lexer.getToken();
        if (token.getType() == Keyword.INSERT) {
            return parseInsert(false);
        } else if (token.getType() == Keyword.DELETE) {
            return parseDelete();
        } else if (token.getType() == Keyword.UPDATE) {
            return parseUpdate(false);
        }
        throw new SQLParseUnsupportedException(token.getType());
    }

    protected SQLStatement parseInsert(boolean prepared) throws SQLParseException, UnterminatedCharException {
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
                if (prepared) {
                    value.add("(none)");
                    accept(Symbol.QUESTION);
                } else {
                    value.add(lexer.getToken().getLiterals());
                    if (equalAny(Literals.INT, Literals.STRING)) {
                        lexer.nextToken();
                    }
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
        whereExpressionParser.parse(statement.getWhereExpression(), false);
        accept(Symbol.SEMI);
        accept(Assist.END);
        return statement;
    }

    protected SQLStatement parseUpdate(boolean prepared) throws SQLParseException, UnterminatedCharException, SQLParseUnsupportedException {
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
            if (prepared) {
                column.setValue("(none)");
                accept(Symbol.QUESTION);
            } else {
                if (equalAny(Literals.INT, Literals.STRING)) {
                    column.setValue(lexer.getToken().getLiterals());
                    lexer.nextToken();
                }
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
        whereExpressionParser.parse(statement.getWhereExpression(), prepared);
        accept(Symbol.SEMI);
        accept(Assist.END);
        return statement;
    }
}
