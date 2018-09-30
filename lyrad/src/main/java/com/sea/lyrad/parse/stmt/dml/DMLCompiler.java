package com.sea.lyrad.parse.stmt.dml;

import com.sea.lyrad.lex.Lexer;
import com.sea.lyrad.lex.analyze.UnterminatedCharException;
import com.sea.lyrad.lex.token.Keyword;
import com.sea.lyrad.lex.token.Token;
import com.sea.lyrad.parse.SQLCompileUnsupportedException;
import com.sea.lyrad.parse.SQLParseException;
import com.sea.lyrad.parse.SQLParseUnsupportedException;
import com.sea.lyrad.parse.stmt.PreparedStatement;

public class DMLCompiler extends DMLParser {
    public DMLCompiler(Lexer lexer) {
        super(lexer);
    }

    public PreparedStatement compile() throws SQLParseUnsupportedException, SQLParseException, UnterminatedCharException, SQLCompileUnsupportedException {
        Token token = lexer.getToken();
        if (token.getType() == Keyword.INSERT) {
            return compileInsert();
        } else if (token.getType() == Keyword.UPDATE) {
            return compileUpdate();
        }
        throw new SQLCompileUnsupportedException(token.getType());
    }

    private PreparedStatement compileInsert() throws SQLParseException, UnterminatedCharException {
        return new PreparedStatement(parseInsert(true));
    }

    private PreparedStatement compileUpdate() throws UnterminatedCharException, SQLParseUnsupportedException, SQLParseException {
        return new PreparedStatement(parseUpdate(true));
    }
}
