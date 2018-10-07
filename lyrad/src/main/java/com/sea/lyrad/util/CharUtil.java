package com.sea.lyrad.util;

/**
 * 字符工具
 */
public class CharUtil {

    /**
     * End of input.
     * Ctrl+Z
     */
    private static final byte EOI = 0x1A;

    private CharUtil() {
    }

    public static char getEOI() {
        return (char) EOI;
    }

    public static boolean isWhitespace(char ch) {
        return ch <= 0x20 && ch != EOI || ch >= 0x7F && ch <= 0xA0;
    }

    public static boolean isEndOfInput(char ch) {
        return ch == EOI;
    }

    public static boolean isAlphabet(char ch) {
        return ch >= 'A' && ch <= 'Z' || ch >= 'a' && ch <= 'z';
    }

    public static boolean isDigital(char ch) {
        return ch >= '0' && ch <= '9';
    }

    public static boolean isSymbol(char ch) {
        return ch == '(' || ch == ')'
                || ch == '[' || ch == ']'
                || ch == '{' || ch == '}'
                || ch == '+' || ch == '-'
                || ch == '*' || ch == '/'
                || ch == '%' || ch == '^'
                || ch == '=' || ch == '>'
                || ch == '<' || ch == '~'
                || ch == '!' || ch == '?'
                || ch == '&' || ch == '|'
                || ch == '.' || ch == ':'
                || ch == '#' || ch == ','
                || ch == ';';
    }
}
