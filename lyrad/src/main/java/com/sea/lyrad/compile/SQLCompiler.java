package com.sea.lyrad.compile;

import com.sea.lyrad.lex.analyze.UnterminatedCharException;
import com.sea.lyrad.parse.SQLParseException;
import com.sea.lyrad.parse.SQLParseUnsupportedException;
import com.sea.lyrad.stmt.PreparedStatement;

public interface SQLCompiler {
    PreparedStatement compile() throws SQLParseUnsupportedException, SQLParseException, UnterminatedCharException, SQLCompileUnsupportedException;
}
