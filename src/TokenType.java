public enum TokenType {
    // literals
    IDENTIFIER, NUMBER,
    
    // ops
    PLUS, MINUS, MULTIPLY, DIVIDE, EQUALS,
    
    // punctuation
    LEFT_PAREN,
    RIGHT_PAREN,
    LEFT_BRACE,
    RIGHT_BRACE,
    SEMICOLON,

    // keywords
    LET,
    IF,
    ELSE,
    FUNC,
    RETURN,

    // eof, unknown
    EOF,
    UNKNOWN
}