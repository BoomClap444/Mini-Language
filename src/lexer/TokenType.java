package lexer;

public enum TokenType {
    // literals
    IDENTIFIER, NUMBER, STRING,
    
    // ops
    PLUS, MINUS, MULTIPLY, DIVIDE, EQUALS,

    // comparators
    LESS_THAN,
    GREATER_THAN,
    LESS_EQUALS,
    GREATER_EQUALS,
    EQUALS_EQUALS,
    NOT_EQUALS,
    NOT,
    AND,
    OR,
    
    // punctuation
    LEFT_PAREN,
    RIGHT_PAREN,
    LEFT_BRACE,
    RIGHT_BRACE,
    LEFT_BRACKET,
    RIGHT_BRACKET,
    SEMICOLON,
    COMMA,

    // keywords
    LET,
    IF,
    ELSE,
    FUNC,
    RETURN,
    WHILE,
    FOR,
    TRUE,
    FALSE,

    // eof, unknown
    EOF,
    UNKNOWN
}