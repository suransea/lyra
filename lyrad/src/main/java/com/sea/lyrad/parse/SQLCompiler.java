package com.sea.lyrad.parse;

import com.sea.lyrad.lex.Lexer;
import com.sea.lyrad.lex.analyze.UnterminatedCharException;
import com.sea.lyrad.lex.token.Keyword;
import com.sea.lyrad.lex.token.Token;
import com.sea.lyrad.parse.stmt.PreparedStatement;
import com.sea.lyrad.parse.stmt.dml.DMLCompiler;
import com.sea.lyrad.parse.stmt.dql.DQLCompiler;

public class SQLCompiler extends SQLParser {
    public SQLCompiler(Lexer lexer) {
        super(lexer);
    }

    public PreparedStatement compile() throws SQLParseException, UnterminatedCharException, SQLCompileUnsupportedException, SQLParseUnsupportedException {
        Token token = lexer.nextToken();
        if (equalAny(Keyword.SELECT)) {
            return new DQLCompiler(lexer).compile();
        } else if (equalAny(Keyword.INSERT, Keyword.UPDATE)) {
            return new DMLCompiler(lexer).compile();
        } else {
            throw new SQLCompileUnsupportedException(token.getType());
        }
    }
}
