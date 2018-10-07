package com.sea.lyrad.compile;

import com.sea.lyrad.lex.Lexer;
import com.sea.lyrad.lex.analyze.UnterminatedCharException;
import com.sea.lyrad.lex.token.Keyword;
import com.sea.lyrad.lex.token.Token;
import com.sea.lyrad.lex.token.TokenType;
import com.sea.lyrad.parse.SQLParseException;

public class SQLCompilerFactory {
    private Lexer lexer;

    public SQLCompilerFactory() {
    }

    /**
     * 创建SQL编译器实例
     *
     * @param sql 语句
     * @return sql编译器
     */
    public SQLCompiler createInstance(String sql) throws SQLParseException, UnterminatedCharException, SQLCompileUnsupportedException {
        lexer = new Lexer(sql);
        Token token = lexer.nextToken();
        if (equalAny(Keyword.SELECT)) {
            return new DQLCompiler(lexer);
        } else if (equalAny(Keyword.INSERT, Keyword.UPDATE)) {
            return new DMLCompiler(lexer);
        } else {
            throw new SQLCompileUnsupportedException(token.getType());
        }
    }

    /**
     * 当前词素类型是否与目标词素类型列表之一匹配
     *
     * @param tokenTypes 词素类型列表
     * @return true if matched
     */
    private boolean equalAny(TokenType... tokenTypes) {
        for (TokenType each : tokenTypes) {
            if (each == lexer.getToken().getType()) {
                return true;
            }
        }
        return false;
    }
}
