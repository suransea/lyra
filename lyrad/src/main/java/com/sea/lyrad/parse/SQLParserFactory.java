package com.sea.lyrad.parse;

import com.sea.lyrad.lex.Lexer;
import com.sea.lyrad.lex.analyze.UnterminatedCharException;
import com.sea.lyrad.lex.token.Keyword;
import com.sea.lyrad.lex.token.Token;
import com.sea.lyrad.lex.token.TokenType;
import com.sea.lyrad.parse.stmt.*;

/**
 * SQL解析器工厂
 */
public class SQLParserFactory {
    private Lexer lexer;

    public SQLParserFactory() {
    }

    /**
     * 根据指定sql创建对应的sql解析器对象
     *
     * @param sql sql语句
     * @return sql解析器
     * @throws SQLParseException            无法解析的词素类型
     * @throws UnterminatedCharException    有开始却无匹配的结束字符（串）
     * @throws SQLParseUnsupportedException 不支持的sql语句
     */
    public SQLParser createInstance(String sql) throws SQLParseException, UnterminatedCharException, SQLParseUnsupportedException {
        lexer = new Lexer(sql);
        Token token = lexer.nextToken();
        if (equalAny(Keyword.USE, Keyword.SHOW, Keyword.DESCRIBE)) {
            return new DALParser(lexer);
        } else if (equalAny(Keyword.TRUNCATE)) {
            return new DDLParser(lexer);
        } else if (equalAny(Keyword.CREATE, Keyword.ALTER, Keyword.DROP)) {
            lexer.nextToken();
            Lexer newLexer = new Lexer(lexer.getContent());
            newLexer.nextToken();
            if (equalAny(Keyword.USER)) {
                return new DCLParser(newLexer);
            } else {
                return new DDLParser(newLexer);
            }
        } else if (equalAny(Keyword.DENY, Keyword.GRANT)) {
            return new DCLParser(lexer);
        } else if (equalAny(Keyword.INSERT, Keyword.DELETE, Keyword.UPDATE)) {
            return new DMLParser(lexer);
        } else if (equalAny(Keyword.SELECT)) {
            return new DQLParser(lexer);
        } else {
            throw new SQLParseUnsupportedException(token.getType());
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
