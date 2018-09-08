package com.sea.lyrad.parse.stmt.dql;

import com.sea.lyrad.lex.Lexer;
import com.sea.lyrad.lex.analyze.UnterminatedCharException;
import com.sea.lyrad.lex.token.*;
import com.sea.lyrad.parse.SQLParseException;
import com.sea.lyrad.parse.SQLParseUnsupportedException;
import com.sea.lyrad.parse.SQLParser;
import com.sea.lyrad.parse.stmt.SQLStatement;
import com.sea.lyrad.parse.stmt.context.Column;
import com.sea.lyrad.parse.stmt.context.OrderExpression;

public class DQLParser extends SQLParser {
    public DQLParser(Lexer lexer) {
        super(lexer);
    }

    public SQLStatement parse() throws SQLParseUnsupportedException, SQLParseException, UnterminatedCharException {
        Token token = lexer.getToken();
        if (token.getType() == Keyword.SELECT) {
            return parseSelect();
        }
        throw new SQLParseUnsupportedException(token.getType());
    }

    private SQLStatement parseSelect() throws SQLParseException, UnterminatedCharException, SQLParseUnsupportedException {
        accept(Keyword.SELECT);
        SelectStatement statement = new SelectStatement(lexer.getContent());
        if (lexer.getToken().getType() == Symbol.STAR) {
            statement.setStar(true);
            accept(Symbol.STAR);
        } else {
            while (true) {
                Column column = new Column();
                column.setColumnName(lexer.getToken().getLiterals());
                accept(Literals.IDENTIFIER);
                if (lexer.getToken().getType() != Symbol.COMMA) {
                    break;
                }
                accept(Symbol.COMMA);
            }
        }
        accept(Keyword.FROM);
        statement.setTableName(lexer.getToken().getLiterals());
        accept(Literals.IDENTIFIER);
        if (lexer.getToken().getType() == Symbol.SEMI) {
            accept(Symbol.SEMI);
            accept(Assist.END);
            return statement;
        }
        parseWhere(statement);
        if (lexer.getToken().getType() == Symbol.SEMI) {
            accept(Symbol.SEMI);
            accept(Assist.END);
            return statement;
        }
        statement.setOrderExpression(parseOrder());
        accept(Symbol.SEMI);
        accept(Assist.END);
        return statement;
    }

    private OrderExpression parseOrder() throws SQLParseException, UnterminatedCharException {
        accept(Keyword.ORDER);
        accept(Keyword.BY);
        OrderExpression orderExpression = new OrderExpression();
        while (true) {
            Column column = new Column();
            column.setColumnName(lexer.getToken().getLiterals());
            accept(Literals.IDENTIFIER);
            orderExpression.getColumns().add(column);
            if (lexer.getToken().getType() != Symbol.COMMA) {
                break;
            }
            accept(Symbol.COMMA);
        }
        if (equalAny(Keyword.ASC, Keyword.DESC)) {
            if (equalAny(Keyword.DESC)) {
                orderExpression.setAsc(false);
            }
            lexer.nextToken();
        }
        return orderExpression;
    }
}
