import java.util.List;

public class Parser {
    private final List<Token> tokens;
    private int pos;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        this.pos = 0;
    }

    public Token currentToken() {
        return tokens.get(this.pos);
    }

    public void advance() {
        this.pos++;
    }

    private LetStatement processLetStatement() {
        advance(); // LET
        
        String variableName = currentToken().getText();
        expect(TokenType.IDENTIFIER);

        expect(TokenType.EQUALS);

        ASTNode value = parseExpression();

        expect(TokenType.SEMICOLON);

        return new LetStatement(variableName, value);
    }

    private void expect(TokenType type) {
        if (currentToken().getType() != type) {
            throw new IllegalArgumentException(
                "Expected " + type + ", got " + currentToken().getType()
            );
        }

        advance();
    }
}
