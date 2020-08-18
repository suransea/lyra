package com.sea.lyrad.lex.analyze;

import com.sea.lyrad.lex.token.Keyword;
import com.sea.lyrad.lex.token.TokenType;

import java.util.HashMap;
import java.util.Map;

public class Dictionary {
    private Map<String, Keyword> tokens = new HashMap<>(1024);

    public Dictionary() {
        for (Keyword each : Keyword.values()) {
            tokens.put(each.name(), each);
        }
    }

    /**
     * 根据词素类型名称返回类型
     *
     * @param literals         类型名
     * @param defaultTokenType 默认类型
     * @return type of literals or default type
     */
    TokenType findTokenType(String literals, TokenType defaultTokenType) {
        String key = null == literals ? null : literals.toUpperCase();
        return tokens.containsKey(key) ? tokens.get(key) : defaultTokenType;
    }

    /**
     * 根据词素类型名称返回类型
     *
     * @param literals 类型名
     * @return type
     */
    TokenType findTokenType(String literals) {
        String key = null == literals ? null : literals.toUpperCase();
        if (tokens.containsKey(key)) {
            return tokens.get(key);
        }
        throw new IllegalArgumentException();
    }
}
