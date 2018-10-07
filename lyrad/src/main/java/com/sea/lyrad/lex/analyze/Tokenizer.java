package com.sea.lyrad.lex.analyze;

import com.sea.lyrad.lex.token.*;
import com.sea.lyrad.util.CharUtil;

/**
 * 分词器
 */
public class Tokenizer {

    private String content;
    private Dictionary dictionary;

    public Tokenizer(String content) {
        this.content = content;
        this.dictionary = new Dictionary();
    }

    private char charAt(int index) {
        return index >= content.length() ? CharUtil.getEOI() : content.charAt(index);
    }

    /**
     * eat whitespace
     *
     * @param start position
     * @return new position
     */
    public int eatWhitespace(int start) {
        int length = 0;
        while (CharUtil.isWhitespace(charAt(start + length))) {
            length++;
        }
        return start + length;
    }

    /**
     * eat comment
     *
     * @param start position
     * @return new position
     * @throws UnterminatedCharException 注释未关闭
     */
    public int eatComment(int start) throws UnterminatedCharException {
        char current = charAt(start);
        char next = charAt(start + 1);
        if (current == '#') {
            return eatSingleLineComment(start, 1);
        } else if (isSingleLineCommentBegin(current, next)) {
            return eatSingleLineComment(start, 2);
        } else if (isMultipleLineCommentBegin(current, next)) {
            return eatMultiLineComment(start);
        }
        return start;
    }

    private boolean isSingleLineCommentBegin(char current, char next) {
        return current == '/' && next == '/' || current == '-' && next == '-';
    }

    private int eatSingleLineComment(int start, int commentSymbolLength) {
        int length = commentSymbolLength;
        while (!CharUtil.isEndOfInput(charAt(start + length)) && '\n' != charAt(start + length)) {
            length++;
        }
        return start + length + 1;
    }

    private boolean isMultipleLineCommentBegin(char current, char next) {
        return current == '/' && next == '*';
    }

    private boolean isMultipleLineCommentEnd(char current, char next) {
        return current == '*' && next == '/';
    }

    private int eatMultiLineComment(int start) throws UnterminatedCharException {
        return untilCommentAndHintTerminateSign(start, 2);
    }

    /**
     * eat hint
     *
     * @param start position
     * @return new position
     * @throws UnterminatedCharException unterminated
     */
    public int eatHint(int start) throws UnterminatedCharException {
        return untilCommentAndHintTerminateSign(start, 3);
    }

    private int untilCommentAndHintTerminateSign(int start, int beginSymbolLength) throws UnterminatedCharException {
        int length = beginSymbolLength;
        while (!isMultipleLineCommentEnd(charAt(start + length), charAt(start + length + 1))) {
            if (CharUtil.isEndOfInput(charAt(start + length))) {
                throw new UnterminatedCharException("*/");
            }
            length++;
        }
        return start + length + 2;
    }

    /**
     * eat variable
     *
     * @param start position
     * @return 词素
     */
    public Token eatVariable(int start) {
        int length = 1;
        if ('@' == charAt(start + 1)) {
            length++;
        }
        while (isVariableChar(start, charAt(start + length))) {
            length++;
        }
        return new Token(Literals.VARIABLE, content.substring(start, start + length), start + length);
    }

    private boolean isVariableChar(int pos, char ch) {
        return isIdentifierChar(ch) || ch == '.';
    }

    private boolean isEscapeChar(int pos, char charIdentifier) {
        return charIdentifier == charAt(pos) && charIdentifier == charAt(pos + 1);
    }

    private boolean isIdentifierChar(char ch) {
        return CharUtil.isAlphabet(ch) || CharUtil.isDigital(ch) || ch == '_' || ch == '$' || ch == '#';
    }

    private boolean isAmbiguousIdentifier(String literals) {
        return Keyword.ORDER.name().equalsIgnoreCase(literals) || Keyword.GROUP.name().equalsIgnoreCase(literals);
    }

    private int getLengthUntilTerminatedChar(int start, char terminatedChar) throws UnterminatedCharException {
        int length = 1;
        while (terminatedChar != charAt(start + length) || isEscapeChar(start + length, terminatedChar)) {
            if (start + length >= content.length()) {
                throw new UnterminatedCharException(terminatedChar);
            }
            if (isEscapeChar(start + length, terminatedChar)) {
                length++;
            }
            length++;
        }
        return length + 1;
    }

    /**
     * eat 标识符
     *
     * @param start position
     * @return 词素
     * @throws UnterminatedCharException unterminated
     */
    public Token eatIdentifier(int start) throws UnterminatedCharException {
        if ('`' == charAt(start)) {
            int length = getLengthUntilTerminatedChar(start, '`');
            return new Token(Literals.IDENTIFIER, content.substring(start, start + length), start + length);
        }
        if ('"' == charAt(start)) {
            int length = getLengthUntilTerminatedChar(start, '"');
            return new Token(Literals.IDENTIFIER, content.substring(start, start + length), start + length);
        }
        if ('[' == charAt(start)) {
            int length = getLengthUntilTerminatedChar(start, ']');
            return new Token(Literals.IDENTIFIER, content.substring(start, start + length), start + length);
        }
        int length = 0;
        while (isIdentifierChar(charAt(start + length))) {
            length++;
        }
        String literals = content.substring(start, start + length);
        if (isAmbiguousIdentifier(literals)) {
            return new Token(processAmbiguousIdentifier(start + length, literals), literals, start + length);
        }
        return new Token(dictionary.findTokenType(literals, Literals.IDENTIFIER), literals, start + length);
    }

    /**
     * 获取引起歧义的标识符词法标记类型
     *
     * @param start    position
     * @param literals 标识符
     * @return 类型
     */
    private TokenType processAmbiguousIdentifier(int start, String literals) {
        int i = 0;
        while (CharUtil.isWhitespace(charAt(start + i))) {
            i++;
        }
        if (Keyword.BY.name().equalsIgnoreCase(String.valueOf(new char[]{charAt(start + i), charAt(start + i + 1)}))) {
            return dictionary.findTokenType(literals);
        }
        return Literals.IDENTIFIER;
    }

    /**
     * eat hex decimal.
     *
     * @return hex decimal token
     */
    public Token eatHexDecimal(int start) {
        int length = 2;//Begin length of hex
        if ('-' == charAt(start + length)) {
            length++;
        }
        while (isHex(charAt(start + length))) {
            length++;
        }
        return new Token(Literals.HEX, content.substring(start, start + length), start + length);
    }

    private boolean isHex(char ch) {
        return ch >= 'A' && ch <= 'F' || ch >= 'a' && ch <= 'f' || CharUtil.isDigital(ch);
    }

    private int getDigitalLength(int start) {
        int result = 0;
        while (CharUtil.isDigital(charAt(start + result))) {
            result++;
        }
        return result;
    }

    private boolean isScientificNotation(int start) {
        char current = charAt(start);
        return current == 'e' || current == 'E';
    }

    private boolean isBinaryNumber(int start) {
        char current = charAt(start);
        return current == 'f' || current == 'F' || current == 'd' || current == 'D';
    }

    /**
     * eat number.
     *
     * @return number token
     */
    public Token eatNumber(int start) {
        int length = 0;
        if ('-' == charAt(start + length)) {
            length++;
        }
        length += getDigitalLength(start + length);
        boolean isFloat = false;
        if ('.' == charAt(start + length)) {
            isFloat = true;
            length++;
            length += getDigitalLength(start + length);
        }
        if (isScientificNotation(start + length)) {
            isFloat = true;
            length++;
            if ('+' == charAt(start + length) || '-' == charAt(start + length)) {
                length++;
            }
            length += getDigitalLength(start + length);
        }
        if (isBinaryNumber(start + length)) {
            isFloat = true;
            length++;
        }
        return new Token(isFloat ? Literals.FLOAT : Literals.INT, content.substring(start, start + length), start + length);
    }

    /**
     * eat chars.
     *
     * @return string token
     */
    public Token eatString(int start) throws UnterminatedCharException {
        return eatString(start, charAt(start));
    }

    private Token eatString(int start, char terminatedChar) throws UnterminatedCharException {
        int length = getLengthUntilTerminatedChar(start, terminatedChar);
        return new Token(Literals.STRING, content.substring(start + 1, start + length - 1), start + length);
    }

    /**
     * eat symbol.
     *
     * @return symbol token
     */
    public Token eatSymbol(int start) {
        int length = 0;
        while (CharUtil.isSymbol(charAt(start + length))) {
            length++;
        }
        String literals = content.substring(start, start + length);
        Symbol symbol;
        //倒序遍历
        while (null == (symbol = Symbol.literalsOf(literals))) {
            literals = content.substring(start, start + --length);
        }
        return new Token(symbol, literals, start + length);
    }
}
