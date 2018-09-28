package com.sea.lyrad.lex.analyze;

import com.sea.lyrad.lex.LexException;

public class UnterminatedCharException extends LexException {
    private static final String MESSAGE = "Illegal input, unterminated '%s'.";

    public UnterminatedCharException(char terminatedChar) {
        super(String.format(MESSAGE, terminatedChar));
    }

    public UnterminatedCharException(String terminatedChar) {
        super(String.format(MESSAGE, terminatedChar));
    }
}
