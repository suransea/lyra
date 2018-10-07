package com.sea.lyrad.parse.stmt;

import com.sea.lyrad.lex.Lexer;
import com.sea.lyrad.lex.analyze.UnterminatedCharException;
import com.sea.lyrad.lex.token.*;
import com.sea.lyrad.parse.BaseSQLParser;
import com.sea.lyrad.parse.SQLParseException;
import com.sea.lyrad.parse.SQLParseUnsupportedException;
import com.sea.lyrad.parse.SQLParser;
import com.sea.lyrad.stmt.SQLStatement;
import com.sea.lyrad.stmt.dcl.AlterUserStatement;
import com.sea.lyrad.stmt.dcl.CreateUserStatement;

public class DCLParser extends BaseSQLParser implements SQLParser {
    public DCLParser(Lexer lexer) {
        super(lexer);
    }

    @Override
    public SQLStatement parse() throws SQLParseUnsupportedException, SQLParseException, UnterminatedCharException {
        Token token = lexer.getToken();
        if (token.getType() == Keyword.CREATE) {
            return parseCreate();
        } else if (token.getType() == Keyword.ALTER) {
            return parseAlter();
        }
        throw new SQLParseUnsupportedException(lexer.getToken().getType());
    }

    private SQLStatement parseCreate() throws SQLParseException, UnterminatedCharException {
        CreateUserStatement statement = new CreateUserStatement(lexer.getContent());
        accept(Keyword.CREATE);
        accept(Keyword.USER);
        statement.setUsername(lexer.getToken().getLiterals());
        accept(Literals.IDENTIFIER);
        accept(Keyword.IDENTIFIED);
        accept(Keyword.BY);
        statement.setPassword(lexer.getToken().getLiterals());
        accept(Literals.STRING);
        accept(Symbol.SEMI);
        accept(Assist.END);
        return statement;
    }

    private SQLStatement parseAlter() throws SQLParseException, UnterminatedCharException {
        AlterUserStatement statement = new AlterUserStatement(lexer.getContent());
        accept(Keyword.ALTER);
        accept(Keyword.USER);
        statement.setUsername(lexer.getToken().getLiterals());
        accept(Literals.IDENTIFIER);
        accept(Keyword.IDENTIFIED);
        accept(Keyword.BY);
        statement.setPassword(lexer.getToken().getLiterals());
        accept(Literals.STRING);
        accept(Symbol.SEMI);
        accept(Assist.END);
        return statement;
    }
}
