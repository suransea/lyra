package com.sea.lyrad.parse.express;

import com.sea.lyrad.lex.Lexer;
import com.sea.lyrad.lex.analyze.UnterminatedCharException;
import com.sea.lyrad.lex.token.Keyword;
import com.sea.lyrad.lex.token.Literals;
import com.sea.lyrad.lex.token.Symbol;
import com.sea.lyrad.parse.BaseSQLParser;
import com.sea.lyrad.parse.SQLParseException;
import com.sea.lyrad.parse.SQLParseUnsupportedException;
import com.sea.lyrad.stmt.common.Column;
import com.sea.lyrad.stmt.common.Condition;
import com.sea.lyrad.stmt.common.WhereExpression;

import java.util.List;

public class WhereExpressionParser extends BaseSQLParser {
    public WhereExpressionParser(Lexer lexer) {
        super(lexer);
    }

    public void parse(WhereExpression whereExpression, boolean prepared) throws SQLParseException, UnterminatedCharException, SQLParseUnsupportedException {
        List<Condition> conditions = whereExpression.getConditions();
        List<Keyword> connectors = whereExpression.getConnectors();
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
            if (prepared) {
                condition.setValue("(none)");
                accept(Symbol.QUESTION);
            } else {
                if (equalAny(Literals.STRING, Literals.INT)) {
                    condition.setValue(lexer.getToken().getLiterals());
                    lexer.nextToken();
                } else {
                    throw new SQLParseUnsupportedException(lexer.getToken().getType());
                }
            }
            conditions.add(condition);
            if (!equalAny(Keyword.AND, Keyword.OR)) {
                break;
            } else {
                connectors.add((Keyword) lexer.getToken().getType());
                lexer.nextToken();
            }
        }
    }
}
