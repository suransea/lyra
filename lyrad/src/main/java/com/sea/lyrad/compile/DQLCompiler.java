package com.sea.lyrad.compile;

import com.sea.lyrad.lex.Lexer;
import com.sea.lyrad.lex.analyze.UnterminatedCharException;
import com.sea.lyrad.lex.token.Keyword;
import com.sea.lyrad.lex.token.Token;
import com.sea.lyrad.parse.SQLParseException;
import com.sea.lyrad.parse.SQLParseUnsupportedException;
import com.sea.lyrad.parse.stmt.DQLParser;
import com.sea.lyrad.stmt.PreparedStatement;

public class DQLCompiler extends DQLParser implements SQLCompiler {
    public DQLCompiler(Lexer lexer) {
        super(lexer);
    }

    public PreparedStatement compile() throws SQLParseUnsupportedException, SQLParseException, UnterminatedCharException, SQLCompileUnsupportedException {
        Token token = lexer.getToken();
        if (token.getType() == Keyword.SELECT) {
            return compileSelect();
        }
        throw new SQLCompileUnsupportedException(token.getType());
    }

    private PreparedStatement compileSelect() throws UnterminatedCharException, SQLParseUnsupportedException, SQLParseException {
        return new PreparedStatement(parseSelect(true));
    }
}
