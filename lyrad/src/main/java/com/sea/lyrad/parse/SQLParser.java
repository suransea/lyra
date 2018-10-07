package com.sea.lyrad.parse;

import com.sea.lyrad.compile.SQLCompileUnsupportedException;
import com.sea.lyrad.lex.analyze.UnterminatedCharException;
import com.sea.lyrad.stmt.SQLStatement;

/**
 * SQL解析器
 */
public interface SQLParser {
    /**
     * 解析sql并返回解析后的语句对象
     *
     * @return statement对象
     */
    SQLStatement parse() throws SQLParseException, UnterminatedCharException, SQLParseUnsupportedException, SQLCompileUnsupportedException;
}

