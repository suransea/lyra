package com.sea.lyrad.parse;

import com.sea.lyrad.lex.Lexer;
import com.sea.lyrad.lex.analyze.UnterminatedCharException;
import com.sea.lyrad.lex.token.Assist;
import com.sea.lyrad.lex.token.TokenType;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 基础的SQL解析器
 * 抽象的，包含基础的sql词素解析
 * 无法直接解析sql语句
 */
public abstract class BaseSQLParser {
    protected Lexer lexer;

    public BaseSQLParser(String sql) {
        lexer = new Lexer(sql);
    }

    public BaseSQLParser(Lexer lexer) {
        this.lexer = lexer;
    }

    /**
     * 接受当前词素并使词法分析器跳转至下一词素
     *
     * @param tokenType 词素类型
     * @throws SQLParseException         词素类型与当前词素不匹配
     * @throws UnterminatedCharException 有开始却无匹配的结束字符（串）
     */
    public void accept(TokenType tokenType) throws SQLParseException, UnterminatedCharException {
        if (lexer.getToken().getType() != tokenType) {
            throw new SQLParseException(lexer, tokenType);
        }
        lexer.nextToken();
    }

    /**
     * 当前词素类型是否与目标词素类型列表之一匹配
     *
     * @param tokenTypes 词素类型列表
     * @return true if matched
     */
    public boolean equalAny(TokenType... tokenTypes) {
        for (TokenType each : tokenTypes) {
            if (each == lexer.getToken().getType()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 如果词素类型符合列表之一则跳过当前词素
     *
     * @param tokenTypes 词素类型列表
     * @return true if skipped
     * @throws SQLParseException         无法解析的词素类型
     * @throws UnterminatedCharException 有开始却无匹配的结束字符（串）
     */
    public boolean skipIfEqual(TokenType... tokenTypes) throws SQLParseException, UnterminatedCharException {
        if (equalAny(tokenTypes)) {
            lexer.nextToken();
            return true;
        }
        return false;
    }

    /**
     * 跳过全部符合列表之一的词素
     *
     * @param tokenTypes 词素类型列表
     * @throws SQLParseException         无法解析的词素类型
     * @throws UnterminatedCharException 有开始却无匹配的结束字符（串）
     */
    public void skipAll(TokenType... tokenTypes) throws SQLParseException, UnterminatedCharException {
        Set<TokenType> tokenTypeSet = new HashSet<>(Arrays.asList(tokenTypes));
        while (tokenTypeSet.contains(lexer.getToken().getType())) {
            lexer.nextToken();
        }
    }

    /**
     * 跳过全部词素直到词素类型与列表之一匹配
     *
     * @param tokenTypes 词素类型列表
     * @throws SQLParseException         无法解析的词素类型
     * @throws UnterminatedCharException 有开始却无匹配的结束字符（串）
     */
    public void skipUntil(TokenType... tokenTypes) throws SQLParseException, UnterminatedCharException {
        Set<TokenType> tokenTypeSet = new HashSet<>(Arrays.asList(tokenTypes));
        tokenTypeSet.add(Assist.END);
        while (!tokenTypeSet.contains(lexer.getToken().getType())) {
            lexer.nextToken();
        }
    }
}
