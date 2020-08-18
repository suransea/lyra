package com.sea.lyrad.lex.analyze;

import com.sea.lyrad.lex.LexException;

/**
 * 无匹配的结束字符（串）
 */
public class UnterminatedCharException extends LexException {
    private static final String MESSAGE = "Illegal input, unterminated '%s'.";

    public UnterminatedCharException(char terminatedChar) {
        super(String.format(MESSAGE, terminatedChar));
    }

    public UnterminatedCharException(String terminatedChar) {
        super(String.format(MESSAGE, terminatedChar));
    }
}
