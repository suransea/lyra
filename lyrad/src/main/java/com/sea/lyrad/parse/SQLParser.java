package com.sea.lyrad.parse;

import com.sea.lyrad.compile.SQLCompileUnsupportedException;
import com.sea.lyrad.lex.analyze.UnterminatedCharException;
import com.sea.lyrad.stmt.SQLStatement;

public interface SQLParser {
    SQLStatement parse() throws SQLParseException, UnterminatedCharException, SQLParseUnsupportedException, SQLCompileUnsupportedException;
}

