package com.sea.lyrad.compile;

import com.sea.lyrad.lex.analyze.UnterminatedCharException;
import com.sea.lyrad.parse.SQLParseException;
import com.sea.lyrad.parse.SQLParseUnsupportedException;
import com.sea.lyrad.stmt.PreparedStatement;

public interface SQLCompiler {
    /**
     * 编译sql语句
     *
     * @return 编译结果：PreparedStatement
     */
    PreparedStatement compile() throws SQLParseUnsupportedException, SQLParseException, UnterminatedCharException, SQLCompileUnsupportedException;
}
